package com.cmdv.core.helpers

import java.text.DecimalFormat

/**
 *
 */
fun formatPrice(value: Float): String =
	DecimalFormat("#,###,###").format(value).replace(',', '.')
/**
 * TODO Improve to get symbol from country.
 */
fun formatPriceWithCurrency(value: Float): String =
	DecimalFormat("$###,###,##0.00").format(value).replace(',', '.')