/*
 * Copyright (c) 2019. dvnlabs.ml , Davin Alfarizky Putra Basudewa
 * Email : dbasudewa@gmail.com / moshi2_davin@dvnlabs.ml
 * UnSikadu source code for Android (tm) ,
 * Internal License Only,NOT FOR REDISTRIBUTE
 */


package ml.dvnlabs.unsikadu.view.list.grade

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import ml.dvnlabs.unsikadu.model.GradeCourseSemester

import ml.dvnlabs.unsikadu.R
import net.cachapa.expandablelayout.ExpandableLayout

class gradeCourseListHolder(context: Context,itemView : View) : RecyclerView.ViewHolder(itemView),View.OnClickListener{

    var NumText : TextView = itemView.findViewById(R.id.gradeCourseNum)
    var NameText : TextView = itemView.findViewById(R.id.gradeCourseName)
    var ScoreText : TextView = itemView.findViewById(R.id.gradeCourseScore)

    var sksText : TextView = itemView.findViewById(R.id.gradeCourseSKS)
    var gradeText : TextView = itemView.findViewById(R.id.gradeCourseGrade)
    var absentText : TextView = itemView.findViewById(R.id.gradeCourseAbsent)
    var gtotalText : TextView = itemView.findViewById(R.id.gradeCourseGrandTotal)
    var quizText : TextView = itemView.findViewById(R.id.gradeCourseQuiz)
    var tugasText : TextView = itemView.findViewById(R.id.gradeCourseTugas)
    var examText : TextView = itemView.findViewById(R.id.gradeCourseExam)


    var container : MaterialCardView = itemView.findViewById(R.id.gradeCourseContainer)
    var expand : ExpandableLayout = itemView.findViewById(R.id.gradeCourseExpand)

    init {
        container.setOnClickListener(this)
    }

    fun bind(model : GradeCourseSemester){
        NumText.text = model.Num.toString()
        NameText.text = model.CourseN
        NameText.isSelected = true
        ScoreText.text = model.GradeLetter

        sksText.text = model.Credit.toString()
        val mid = model.MidTerm.toString()
        val last = model.LastTerm.toString()
        val letter = model.GradeLetter
        val number = model.GradePoint
        gradeText.text = "$letter / $number"
        absentText.text = model.Avail.toString()
        val total = model.Avail + model.Quiz + model.Assignment + model.MidTerm + model.LastTerm
        gtotalText.text = (total / 5).toString()
        quizText.text = model.Quiz.toString()
        tugasText.text = model.Assignment.toString()
        examText.text = "$mid / $last"

    }

    override fun onClick(v: View?) {
        expand.toggle()
    }
}