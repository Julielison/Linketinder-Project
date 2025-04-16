package org.linketinder.model

class Skill {
    Integer id
    String name

    Skill(Integer id, String name) {
        this.id = id
        this.name = name
    }

    @Override
    String toString() {
        return """
        Id: $id
        Nome: $name"""
    }
}

