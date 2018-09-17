package com.example.hgtxxgl.application.bean.temp;

import java.util.List;

//Api_Get_PeopleInfoSim
//得到人员简单信息

public class TempPeopleInfoBean {

    private List<ApiGetPeopleInfoSimBean> Api_Get_PeopleInfoSim;

    public List<ApiGetPeopleInfoSimBean> getApi_Get_PeopleInfoSim() {
        return Api_Get_PeopleInfoSim;
    }

    public void setApi_Get_PeopleInfoSim(List<ApiGetPeopleInfoSimBean> Api_Get_PeopleInfoSim) {
        this.Api_Get_PeopleInfoSim = Api_Get_PeopleInfoSim;
    }

    public static class ApiGetPeopleInfoSimBean {
        /**
         * Count : 1
         * Department : 测试部门B
         * Name : 部门B员工1
         * No : 54969542
         * Sex : 1
         * Unit : 测试单位B
         */

        private String Count;
        private String Department;
        private String Name;
        private String No;
        private String Sex;
        private String Unit;

        public String getCount() {
            return Count;
        }

        public void setCount(String Count) {
            this.Count = Count;
        }

        public String getDepartment() {
            return Department;
        }

        public void setDepartment(String Department) {
            this.Department = Department;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getNo() {
            return No;
        }

        public void setNo(String No) {
            this.No = No;
        }

        public String getSex() {
            return Sex;
        }

        public void setSex(String Sex) {
            this.Sex = Sex;
        }

        public String getUnit() {
            return Unit;
        }

        public void setUnit(String Unit) {
            this.Unit = Unit;
        }
    }
}
