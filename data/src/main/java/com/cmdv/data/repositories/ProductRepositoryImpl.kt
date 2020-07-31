package com.cmdv.data.repositories

import androidx.lifecycle.MutableLiveData
import com.cmdv.data.BuildConfig
import com.cmdv.data.entities.firebase.ProductFirebaseEntity
import com.cmdv.data.helpers.FirebaseQueryHelper
import com.cmdv.data.mappers.ProductFirebaseMapper
import com.cmdv.domain.models.*
import com.cmdv.domain.repositories.ProductRepository
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private const val DATE_FORMAT_DD_MM_YY = "dd-MM-yyyy'T'HH:mm:ss.SSS"

class ProductRepositoryImpl : ProductRepository {

    var productMutableLiveData = MutableLiveData<LiveDataStatusWrapper<ProductModel?>>()

    private val dbRootRef: FirebaseDatabase = FirebaseDatabase.getInstance()

    private val dbProductsRef: DatabaseReference = dbRootRef.getReference(BuildConfig.DB_PRODUCTS_ROOT_PATH)

    private val dbProductTypeRef: DatabaseReference = dbRootRef.getReference(BuildConfig.DB_PRODUCT_TYPE_ROOT_PATH)

    override fun updateProduct(
        productMutableLiveData: MutableLiveData<LiveDataStatusWrapper<ProductModel>>,
        id: Int,
        product: ProductModel
    ) {
        // Set loading status.
        productMutableLiveData.value = LiveDataStatusWrapper.loading(null)

        val productFirebase: ProductFirebaseEntity =
            ProductFirebaseMapper().transformModelToEntity(product)
        dbProductsRef.child(id.toString()).setValue(
            productFirebase
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                productMutableLiveData.value =
                    LiveDataStatusWrapper.success(
                        ProductFirebaseMapper().transformEntityToModel(productFirebase)
                    )
            } else {
                productMutableLiveData.value =
                    LiveDataStatusWrapper.error("There was an error trying to update product with id-$id.", null)
            }
        }
    }

    override fun createProduct(
        name: String,
        description: String,
        productType: String,
        costPrice: String,
        originalPrice: String,
        sellingPrice: String,
        quantity: Int,
        colorQuantities: ArrayList<ColorQuantityModel>,
        lowBarrier: Int,
        tags: List<String>
    ): MutableLiveData<LiveDataStatusWrapper<ProductModel?>> {
        // Reset Live data object to avoid older values.
        productMutableLiveData = MutableLiveData()
        // Set loading status.
        productMutableLiveData.value = (LiveDataStatusWrapper.loading(null))

        dbProductsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val id: Long = dataSnapshot.childrenCount
                val code: String = generateUniqueRandomProductCode(dataSnapshot)
                val productFirebase: ProductFirebaseEntity =
                    ProductFirebaseMapper().transformModelToEntity(
                        getNewProductModel(
                            code, id, productType, name, description, costPrice, originalPrice, sellingPrice, quantity,
                            colorQuantities, lowBarrier, tags
                        )
                    )

                dbProductsRef.child(id.toString()).setValue(productFirebase)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            productMutableLiveData.value =
                                LiveDataStatusWrapper.success(
                                    ProductFirebaseMapper().transformEntityToModel(productFirebase)
                                )
                        } else {
                            productMutableLiveData.value =
                                LiveDataStatusWrapper.error("", null)
                        }
                    }
            }
        })

        return productMutableLiveData
    }

    override fun getProducts(productsMutableLiveData: MutableLiveData<LiveDataStatusWrapper<List<ProductModel>>>) {
        productsMutableLiveData.value = LiveDataStatusWrapper.loading(null)
        dbProductsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val products = ArrayList<ProductModel>()
                for (ds in snapshot.children) {
                    val productFirebaseEntity: ProductFirebaseEntity? =
                        ds.getValue(ProductFirebaseEntity::class.java)
                    if (productFirebaseEntity != null && productFirebaseEntity.active) {
                        products.add(ProductFirebaseMapper().transformEntityToModel(productFirebaseEntity))
                    }
                }
                productsMutableLiveData.value = LiveDataStatusWrapper.success(products)
            }

            override fun onCancelled(error: DatabaseError) {
                productsMutableLiveData.value = LiveDataStatusWrapper.error("", null)
            }
        })
    }

    override fun getProductTypes(mutableLiveData: MutableLiveData<LiveDataStatusWrapper<ArrayList<String>>>) {
        // Set loading status.
        mutableLiveData.value = (LiveDataStatusWrapper.loading(null))
        // Get product types
        dbProductTypeRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val productTypes: ArrayList<String> = arrayListOf()
                    for (child: DataSnapshot in snapshot.children) {
                        child.getValue(String::class.java)?.let {
                            productTypes.add(it)
                        }
                    }
                    // Sort alphabetically and add dumb first item
                    Collections.sort(productTypes, String.CASE_INSENSITIVE_ORDER)
                    mutableLiveData.value = (LiveDataStatusWrapper.success(productTypes))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                mutableLiveData.value =
                    (LiveDataStatusWrapper.error(error.message, null))
            }
        })
    }

    override suspend fun saleProductsInShopCart(shopCart: ShopCartModel) {
        shopCart.products.forEach { soldProduct ->
            dbProductsRef.child(soldProduct.id).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val productFirebaseEntity: ProductFirebaseEntity? = snapshot.getValue(ProductFirebaseEntity::class.java)
                    val product = ProductFirebaseMapper().transformEntityToModel(productFirebaseEntity!!)
                    val newProduct = modifyProductQuantity(product, soldProduct)

                    dbProductsRef.child(newProduct.id.toString()).setValue(ProductFirebaseMapper().transformModelToEntity(newProduct))
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }

    override fun searchProducts(
        _mutableLiveDataFilteredProduct: MutableLiveData<LiveDataStatusWrapper<List<ProductModel>>>,
        queryString: String
    ) {
        if (queryString.isEmpty()) {
            _mutableLiveDataFilteredProduct.value = LiveDataStatusWrapper.error("Query string can't be empty", null)
            return
        }
        _mutableLiveDataFilteredProduct.value = LiveDataStatusWrapper.loading(null)

        GlobalScope.launch(Dispatchers.IO) {
            delay(1000)
            // Get product types
            dbProductTypeRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val productTypes: ArrayList<String> = arrayListOf()
                    if (snapshot.exists()) {
                        for (child: DataSnapshot in snapshot.children) {
                            child.getValue(String::class.java)?.let { productTypes.add(it) }
                        }
                    }
                    val queryType: FirebaseQueryHelper.ProductQueryType = FirebaseQueryHelper(queryString, productTypes).getProductQueryType()
                    when (queryType) {
                        FirebaseQueryHelper.ProductQueryType.BY_CODE,
                        FirebaseQueryHelper.ProductQueryType.BY_TYPE -> {
                            val firebaseProductQuery: Query = FirebaseQueryHelper(queryString, productTypes).getProductQuery()
                            firebaseProductQuery.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val filteredProducts: ArrayList<ProductModel> = arrayListOf()
                                    // Handle results
                                    snapshot.children.forEach { ds: DataSnapshot ->
                                        val productFirebase: ProductFirebaseEntity? = ds.getValue(ProductFirebaseEntity::class.java)
                                        productFirebase?.let {
                                            if (it.active) { filteredProducts.add(ProductFirebaseMapper().transformEntityToModel(it)) }
                                        }
                                    }
                                    _mutableLiveDataFilteredProduct.value = LiveDataStatusWrapper.success(filteredProducts)
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    // Firebase database query failed.
                                    _mutableLiveDataFilteredProduct.value = LiveDataStatusWrapper.error("There was an error filtering products", null)
                                }
                            })
                        }
                        FirebaseQueryHelper.ProductQueryType.BY_NAME -> {
                            // Get all products and filter by name (if name contains query)
                            dbProductsRef.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val filteredProducts: ArrayList<ProductModel> = arrayListOf()
                                    for (ds in snapshot.children) {
                                        val productFirebaseEntity: ProductFirebaseEntity? = ds.getValue(ProductFirebaseEntity::class.java)
                                        productFirebaseEntity?.let {
                                            if (productFirebaseEntity.active &&
                                                productFirebaseEntity.name != null &&
                                                productFirebaseEntity.name.contains(queryString, ignoreCase = true)) {
                                                filteredProducts.add(ProductFirebaseMapper().transformEntityToModel(productFirebaseEntity))
                                            }
                                        }
                                    }
                                    _mutableLiveDataFilteredProduct.value = LiveDataStatusWrapper.success(filteredProducts)
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    _mutableLiveDataFilteredProduct.value = LiveDataStatusWrapper.error("There was an error filtering products", null)
                                }
                            })
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Fail to fetch Product Types
                    _mutableLiveDataFilteredProduct.value = LiveDataStatusWrapper.error("Mocked Error!", null)
                }
            })
            GlobalScope.launch(Dispatchers.Main) {
                // TODO
            }
        }
    }

    override fun markProductInactive(product: ProductModel) {
        dbProductsRef.child(product.id.toString()).child("active").setValue(false)
    }

    private fun modifyProductQuantity(product: ProductModel, soldProduct: ShopCartModel.ShopCartProductModel): ProductModel =
        product.copy(quantity = getNewQuantity(product, soldProduct))

    private fun getNewQuantity(product: ProductModel, soldProduct: ShopCartModel.ShopCartProductModel): QuantityModel {
        var soldNow = 0
        soldProduct.colorQuantity.forEach {
            soldNow += it.quantity
        }
        val sold: Int = product.quantity.sold + soldNow
        val available: Int = product.quantity.initial - sold

        val colorQuantities: ArrayList<ColorQuantityModel> = arrayListOf()
        product.quantity.colorQuantities.forEach { p ->
            var founded = false
            soldProduct.colorQuantity.forEach { scp ->
                if (!founded) {
                    if (scp.value == p.value) {
                        colorQuantities.add(ColorQuantityModel(p.name, p.value, p.quantity - scp.quantity))
                        founded = true
                    }
                }
            }
            if (!founded) {
                colorQuantities.add(ColorQuantityModel(p.name, p.value, p.quantity))
            }
        }

        return QuantityModel(
            product.quantity.initial,
            available,
            sold,
            product.quantity.lowBarrier,
            colorQuantities
        )
    }

    /**
     * Generates a random code of four digits and return as String.
     * This value must be unique in DB so only one product can have it.
     */
    private fun generateUniqueRandomProductCode(dataSnapshot: DataSnapshot): String {
        val randomCode = (1000..9999).random().toString()
        for (ds in dataSnapshot.children) {
            val productFirebase: ProductFirebaseEntity? =
                ds.getValue(ProductFirebaseEntity::class.java)
            if (productFirebase != null && productFirebase.code.equals(randomCode)) {
                generateUniqueRandomProductCode(dataSnapshot)
            }
        }
        return randomCode
    }

    private fun getNewProductModel(
        code: String,
        id: Long,
        productType: String,
        name: String,
        description: String,
        costPrice: String,
        originalPrice: String,
        sellingPrice: String,
        quantity: Int,
        colorQuantities: ArrayList<ColorQuantityModel>,
        lowBarrier: Int,
        tags: List<String>
    ): ProductModel {

        val dateString = SimpleDateFormat(DATE_FORMAT_DD_MM_YY, Locale.getDefault()).format(Date().time)

        return ProductModel(
            code,
            id,
            true,
            productType,
            name,
            description,
            "temp",
            "temp",
            PriceModel(costPrice, if (originalPrice.isEmpty()) sellingPrice else originalPrice, sellingPrice),
            QuantityModel(quantity, quantity, 0, lowBarrier, colorQuantities),
            tags,
            DateModel(dateString, dateString)
        )
    }
}