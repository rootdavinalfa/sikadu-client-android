/*
 * Copyright (c) 2020. dvnlabs.ml , Davin Alfarizky Putra Basudewa
 * Email : dbasudewa@gmail.com / moshi2_davin@dvnlabs.ml
 * UnSikadu source code for Android (tm) ,
 * Internal License Only,NOT FOR REDISTRIBUTE
 */


package ml.dvnlabs.unsikadu.ui.view.tableview.schedule

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.evrencoskun.tableview.adapter.AbstractTableAdapter
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import ml.dvnlabs.unsikadu.R
import ml.dvnlabs.unsikadu.ui.view.tableview.schedule.model.Cell
import ml.dvnlabs.unsikadu.ui.view.tableview.schedule.model.ColumnHeader
import ml.dvnlabs.unsikadu.ui.view.tableview.schedule.model.RowHeader


//ColumnHeader
class ScheduleColumnHead(itemView: View) : AbstractViewHolder(itemView){
    var columnText : TextView = itemView.findViewById(R.id.mColumnText)
    var columnContainer : LinearLayout = itemView.findViewById(R.id.mColumnContainer)
}

//Row header
class ScheduleRowHead(itemView: View) : AbstractViewHolder(itemView){
    var rowText : TextView = itemView.findViewById(R.id.mRowText)
}

//Cell
class ScheduleCell(itemView: View): AbstractViewHolder(itemView){
    var cellText : TextView = itemView.findViewById(R.id.mCellText)
    var cellContainer : LinearLayout = itemView.findViewById(R.id.mCellContainer)
}


class ScheduleTableAdapter(context: Context) : AbstractTableAdapter<ColumnHeader, RowHeader, Cell>(
    context
) {
    override fun onCreateColumnHeaderViewHolder(parent: ViewGroup?, viewType: Int): AbstractViewHolder {
        val layout = LayoutInflater.from(mContext).inflate(R.layout.m_column_layout,parent,false)
        return ScheduleColumnHead(layout)
    }

    override fun onCreateRowHeaderViewHolder(parent: ViewGroup?, viewType: Int): AbstractViewHolder {
        val layout = LayoutInflater.from(mContext).inflate(R.layout.m_row_layout,parent,false)
        return ScheduleRowHead(layout)
    }

    override fun onCreateCellViewHolder(parent: ViewGroup?, viewType: Int): AbstractViewHolder {
        val layout = LayoutInflater.from(mContext).inflate(R.layout.m_cell_layout,parent,false)
        return ScheduleCell(layout)
    }

    @SuppressLint("InflateParams")
    override fun onCreateCornerView(): View {
        return LayoutInflater.from(mContext).inflate(R.layout.m_view_corner,null)
    }

    override fun getColumnHeaderItemViewType(position: Int): Int {
        return 0
    }

    override fun getRowHeaderItemViewType(position: Int): Int {
        return 0
    }

    override fun onBindColumnHeaderViewHolder(
        holder: AbstractViewHolder?,
        columnHeaderItemModel: Any?,
        columnPosition: Int
    ) {
        val columnHeader =
            columnHeaderItemModel as ColumnHeader

        val columnHeaderViewHolder: ScheduleColumnHead = holder as ScheduleColumnHead
        columnHeaderViewHolder.columnText.text= columnHeader.data.toString()

        columnHeaderViewHolder.columnContainer.layoutParams.width = LinearLayout
            .LayoutParams.WRAP_CONTENT
        columnHeaderViewHolder.columnText.requestLayout()

    }

    override fun onBindRowHeaderViewHolder(holder: AbstractViewHolder?, rowHeaderItemModel: Any?, rowPosition: Int) {
        val rowHeader =
            rowHeaderItemModel as RowHeader

        val rowHeaderViewHolder: ScheduleRowHead = holder as ScheduleRowHead
        rowHeaderViewHolder.rowText.text= rowHeader.data.toString()
    }

    override fun onBindCellViewHolder(
        holder: AbstractViewHolder?,
        cellItemModel: Any?,
        columnPosition: Int,
        rowPosition: Int
    ) {
        val cell: Cell = cellItemModel as Cell
        val viewHolder : ScheduleCell = holder as ScheduleCell
        viewHolder.cellText.text = cell.data.toString()
        viewHolder.itemView.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        viewHolder.cellText.requestLayout();
    }

    override fun getCellItemViewType(position: Int): Int {
        return 0
    }
}