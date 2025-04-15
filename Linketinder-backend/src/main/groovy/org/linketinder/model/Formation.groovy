package org.linketinder.model

import org.linketinder.util.Util

import java.time.LocalDate

class Formation {
    Integer id
    String institution
    String nameCourse
    LocalDate dateStart
    LocalDate dateEnd

    Formation(Integer id,
              String institution,
              String nameCourse,
              LocalDate dateStart,
              LocalDate dateEnd)
    {
        this.id = id
        this.institution = institution
        this.nameCourse = nameCourse
        this.dateStart = dateStart
        this.dateEnd = dateEnd
    }

    @Override
    String toString() {
        String period = "${Util.convertToBrFormat(dateStart)} - ${Util.convertToBrFormat(dateEnd)}"
        return "${nameCourse} - ${institution} (${period})"
    }
}