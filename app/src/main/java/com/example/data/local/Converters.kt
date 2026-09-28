package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.CognitiveLevel
import com.example.data.model.GradeLevel
import com.example.data.model.QuestionType
import com.example.data.model.SubjectType

class ExamTypeConverters {
    @TypeConverter
    fun fromSubject(subject: SubjectType): String = subject.name

    @TypeConverter
    fun toSubject(value: String): SubjectType = try {
        SubjectType.valueOf(value)
    } catch (_: Exception) {
        SubjectType.KHTN
    }

    @TypeConverter
    fun fromGrade(grade: GradeLevel): String = grade.name

    @TypeConverter
    fun toGrade(value: String): GradeLevel = try {
        GradeLevel.valueOf(value)
    } catch (_: Exception) {
        GradeLevel.GRADE_8
    }

    @TypeConverter
    fun fromCognitiveLevel(level: CognitiveLevel): String = level.name

    @TypeConverter
    fun toCognitiveLevel(value: String): CognitiveLevel = try {
        CognitiveLevel.valueOf(value)
    } catch (_: Exception) {
        CognitiveLevel.RECOGNITION
    }

    @TypeConverter
    fun fromQuestionType(type: QuestionType): String = type.name

    @TypeConverter
    fun toQuestionType(value: String): QuestionType = try {
        QuestionType.valueOf(value)
    } catch (_: Exception) {
        QuestionType.MULTIPLE_CHOICE_4
    }
}
