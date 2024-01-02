package com.example.seoulf3

object FirstSpellCheck {
    fun firstSpellCheckAndReturn(firstSpell: String): String {

        fun pickSpell(spell: String): String {
            return when(spell) {
                "ㄱ" -> return "가"
                "ㄴ" -> return "나"
                "ㄷ" -> return "다"
                "ㄹ" -> return "라"
                "ㅁ" -> return "마"
                "ㅂ" -> return "바"
                "ㅅ" -> return "사"
                "ㅇ" -> return "아"
                "ㅈ" -> return "자"
                "ㅊ" -> return "차"
                "ㅌ" -> return "타"
                "ㅍ" -> return "파"
                "ㅊ" -> return "차"
                "ㅎ" -> return "하"
                else -> return spell
            }
        }
        return when (val spell = firstSpell[0].toChar()) {
            in 'ㄱ'..'ㅣ' -> return pickSpell(spell.toString())
            in '가'..'낗' -> return "가"
            in '나'..'닣' -> return "나"
            in '다'..'띻' -> return "다"
            in '라'..'맇' -> return "라"
            in '마'..'밓' -> return "마"
            in '바'..'삫' -> return "바"
            in '사'..'앃' -> return "사"
            in '아'..'잏' -> return "아"
            in '자'..'찧' -> return "자"
            in '차'..'칳' -> return "차"
            in '카'..'킿' -> return "카"
            in '타'..'팋' -> return "타"
            in '파'..'핗' -> return "파"
            in '하'..'힣' -> return "하"
            in 'A'..'Z' -> return spell.toString()
            in '0'..'9' -> return spell.toString()
            else -> "특수기호"
        }
    }
}