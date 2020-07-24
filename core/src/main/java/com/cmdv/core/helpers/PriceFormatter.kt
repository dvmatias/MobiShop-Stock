package com.cmdv.core.helpers

import java.text.DecimalFormat
import java.text.NumberFormat

/**
 *
 */
fun formatPrice(value: Float): String =
	DecimalFormat("#,###,###").format(value).replace(',', '.')
/**
 * TODO Improve to get symbol from country.
 */
fun formatPriceWithCurrency(value: Float): String =
	NumberFormat.getCurrencyInstance().format(value)