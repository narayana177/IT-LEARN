package com.example.model

enum class ContentType(val displayName: String) {
  NOTES("Notes"),
  QUESTION_PAPER("Question Paper"),
  STUDY_MATERIAL("Study Material"),
  IMPORTANT_QUESTIONS("Important Questions"),
  LAB_MANUAL("Lab Manual"),
  OTHER("Other Document")
}

enum class ExamType(val displayName: String) {
  NONE("General"),
  MID_1("Mid-1 Paper"),
  MID_2("Mid-2 Paper"),
  REGULAR("Regular Exam Paper"),
  SUPPLY("Supply Exam Paper"),
  PREVIOUS("Previous Paper")
}

data class UnitTopic(
  val unitNumber: Int,
  val unitTitle: String,
  val topics: List<String>,
  val overview: String = ""
)

data class Subject(
  val code: String,
  val name: String,
  val semester: Int,
  val isPractical: Boolean,
  val units: List<UnitTopic> = emptyList(),
  val description: String = ""
)

data class SemesterInfo(
  val semesterNumber: Int,
  val title: String,
  val isContentReady: Boolean,
  val notice: String? = null
)
