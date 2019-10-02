/*
 * Copyright (c) 2019. dvnlabs.ml , Davin Alfarizky Putra Basudewa
 * Email : dbasudewa@gmail.com / moshi2_davin@dvnlabs.ml
 * UnSikadu source code for Android (tm) ,
 * Internal License Only,NOT FOR REDISTRIBUTE
 */


package ml.dvnlabs.unsikadu.model

data class StudentInfo(var npm : String,var name : String,var placeBorn : String, var bornDate : String,var gender : String,var religion : String,
                  var phone : String,var email : String,var address : String,var pictureUrl : String,var faculty : String,var major : String,
                  var degree : String,var clas : String,var group : String,var status : String)

data class ScheduleData(var Name : String,var Year : String,var Quart : String)

data class ScheduleDetail(var CourseN : String,var Class : String,var Room : String,var lecturer : String,
                          var Days : String,var semester : Int ,var fromTime : String,var toTime : String)

data class GradeSemester(var Year: String,var Quart: String,var Semester : String,var Periodic : String,
                         var Detail :String,var NumCourse : Int,var Credit : Int,var Cumulative : Double)

data class GradeCourseSemester(var CourseN: String,var GradeLetter : String, var Num : Int,var Credit : Int,
                               var Avail : Double,var Quiz : Double,var Assignment : Double,var MidTerm : Double,
                               var LastTerm : Double,var GradePoint : Int)