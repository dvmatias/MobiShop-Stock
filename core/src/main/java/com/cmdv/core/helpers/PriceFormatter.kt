package com.cmdv.core.helpers

import java.text.DecimalFormat

/**
 *
 */
fun formatPrice(value: Float): String =
	DecimalFormat("#,###,###").format(value).replace(',', '.')
/**
 *
 */
fun formatPriceWithCurrency(value: Float): String =
	DecimalFormat("$#,###").format(value).replace(',', '.')