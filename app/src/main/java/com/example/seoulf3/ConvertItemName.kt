package com.example.seoulf3

class ConvertItemName {
    fun convertItemName(name: String): String {
        var inputName = name
        if (inputName.contains(".")) {
            var _name = ""
            for (c in inputName) {
                if (c == '.') {
                    _name += '^'
                } else {
                    _name += c
                }
            }
            inputName = _name
        }
        if (inputName.contains("#")) {
            var _name = ""
            for (c in inputName) {
                if (c == '#') {
                    _name += '^'
                } else {
                    _name += c
                }
            }
        }
        if (inputName.contains("$")) {
            var _name = ""
            for (c in inputName) {
                if (c == '$') {
                    _name += '^'
                } else {
                    _name += c
                }
            }
        }
        if (inputName.contains("[")) {
            var _name = ""
            for (c in inputName) {
                if (c == '[') {
                    _name += '^'
                } else {
                    _name += c
                }
            }
        }
        if (inputName.contains("]")) {
            var _name = ""
            for (c in inputName) {
                if (c == ']') {
                    _name += '^'
                } else {
                    _name += c
                }
            }
        }
        return inputName
    }
}