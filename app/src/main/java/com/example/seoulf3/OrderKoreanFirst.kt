package com.example.seoulf3

object OrderKoreanFirst {
    private const val LEFT = -1
    private const val RIGHT = 1

    fun compare(left: String, right: String): Int {
        val _left = left.uppercase().filterNot(Char::isWhitespace)
        val _right = right.uppercase().filterNot(Char::isWhitespace)

        val (llen, rlen) = _left.length to _right.length
        val mlen = llen.coerceAtMost(rlen)

        for (i in 0 until mlen) {
            val (lc, rc) = _left[i] to _right[i]

            if (lc != rc) {
                return if (conKoAndEn(lc, rc) || conKoAndNum(lc, rc) || conEnAndNum(lc, rc) || conKoAndSpec(lc, rc)
                ) -(lc - rc) else if (conEnAndSpec(lc, rc) || conNumAndSpec(lc, rc)) {
                    if (isEnglish(lc) || isNumber(lc)) LEFT else RIGHT
                } else {
                    lc - rc
                }
            }
        }

        return llen - rlen
    }

    private fun isEnglish(ch: Char): Boolean = ch in 'A'..'Z'
    private fun isKorean(ch: Char): Boolean = ch in 'ㄱ'..'ㅣ' || ch in '가'..'힣'
    private fun isNumber(ch: Char): Boolean = ch in '0'..'9'
    private fun isSpecial(ch: Char): Boolean =
        ch in '!'..'/' || ch in ':'..'@' || ch in '['..'`' || ch in '{'..'~'

    private fun conKoAndEn(c1: Char, c2: Char) =
        isKorean(c1) && isEnglish(c2) || isEnglish(c1) && isKorean(c2)

    private fun conKoAndNum(c1: Char, c2: Char) =
        isKorean(c1) && isNumber(c2) || isNumber(c1) && isKorean(c2)

    private fun conKoAndSpec(c1: Char, c2: Char) =
        isKorean(c1) && isSpecial(c2) || isSpecial(c1) && isKorean(c2)

    private fun conEnAndNum(c1: Char, c2: Char) =
        isEnglish(c1) && isNumber(c2) || isNumber(c1) && isEnglish(c2)

    private fun conEnAndSpec(c1: Char, c2: Char) =
        isEnglish(c1) && isSpecial(c2) || isSpecial(c1) && isEnglish(c2)

    private fun conNumAndSpec(c1: Char, c2: Char) =
        isNumber(c1) && isSpecial(c2) || isSpecial(c1) && isNumber(c2)

}