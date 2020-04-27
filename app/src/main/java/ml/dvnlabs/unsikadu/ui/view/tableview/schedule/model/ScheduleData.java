
/*
 * Copyright (c) 2019. dvnlabs.ml , Davin Alfarizky Putra Basudewa
 * Email : dbasudewa@gmail.com / moshi2_davin@dvnlabs.ml
 * UnSikadu source code for Android (tm) ,
 * Internal License Only,NOT FOR REDISTRIBUTE
 */

package ml.dvnlabs.unsikadu.ui.view.tableview.schedule.model;

import java.util.ArrayList;
import java.util.List;

import ml.dvnlabs.unsikadu.model.ScheduleDetail;

public class ScheduleData {
    private ArrayList<ScheduleDetail> datas;
    public ScheduleData(ArrayList<ScheduleDetail> data){
        this.datas = data;
    }

    public List<ColumnHeader> getColumnHeader(){
        List<ColumnHeader> list = new ArrayList<>();
        list.add(new ColumnHeader("H1","Mata Kuliah"));
        list.add(new ColumnHeader("H2","Kelas"));
        list.add(new ColumnHeader("H3","Ruangan"));
        list.add(new ColumnHeader("H4","Dosen"));
        list.add(new ColumnHeader("H5","Hari"));
        list.add(new ColumnHeader("H6","Semester"));
        list.add(new ColumnHeader("H7","Dari"));
        list.add(new ColumnHeader("H8","Sampai"));
        return list;
    }

    public List<RowHeader> getRowHeader(){
        List<RowHeader> list = new ArrayList<>();
        for (int i = 0; i< datas.size(); i++){
            list.add(new RowHeader("C"+i,">>"));
        }
        return list;
    }

    public List<List<Cell>> getCell(){
        List<List<Cell>> list = new ArrayList<>();
        for (int i = 0; i< datas.size(); i++){
            List<Cell> cellList = new ArrayList<>();
            cellList.add(new Cell(String.valueOf(i),datas.get(i).component1()));
            cellList.add(new Cell(String.valueOf(i),datas.get(i).component2()));
            cellList.add(new Cell(String.valueOf(i),datas.get(i).component3()));
            cellList.add(new Cell(String.valueOf(i),datas.get(i).component4()));
            cellList.add(new Cell(String.valueOf(i),datas.get(i).component5()));
            cellList.add(new Cell(String.valueOf(i),datas.get(i).component6()));
            cellList.add(new Cell(String.valueOf(i),datas.get(i).component7()));
            cellList.add(new Cell(String.valueOf(i),datas.get(i).component8()));
            list.add(cellList);
        }
        return list;
    }

}
