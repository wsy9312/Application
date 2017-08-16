package com.example.hgtxxgl.application.entity;

import java.util.List;

/**
 * Created by HGTXxgl on 2017/8/16.
 */

public class PeopleEntity {

    private List<PeopleBean> People;

    public List<PeopleBean> getPeople() {
        return People;
    }

    public void setPeople(List<PeopleBean> People) {
        this.People = People;
    }

    public static class PeopleBean {
        private List<PeopleInfoBean> PeopleInfo;
        private List<PeopleLeaveRrdBean> PeopleLeaveRrd;

        public List<PeopleInfoBean> getPeopleInfo() {
            return PeopleInfo;
        }

        public void setPeopleInfo(List<PeopleInfoBean> PeopleInfo) {
            this.PeopleInfo = PeopleInfo;
        }

        public List<PeopleLeaveRrdBean> getPeopleLeaveRrd() {
            return PeopleLeaveRrd;
        }

        public void setPeopleLeaveRrd(List<PeopleLeaveRrdBean> PeopleLeaveRrd) {
            this.PeopleLeaveRrd = PeopleLeaveRrd;
        }

        public static class PeopleInfoBean {
            /**
             * No : 个人编号
             * Name : 张三
             * CardNo : 31233423412
             * Position : 一等兵
             * Sex : 0
             * Unit : 侦察连一队
             * ArmyGroup : 312
             * PhoneNo : 123123123
             * TelNo : 3123213112
             * GroupName : B
             * LoginName : 123
             * Password : 123
             * Authority :
             * ModifyTime : 123
             * RegisterTime : 123
             * NoIndex :
             * AuthenticationNo : 123
             * IsAndroid : 123
             * BeginNum :
             * EndNum :
             */

            private String No;
            private String Name;
            private String CardNo;
            private String Position;
            private int Sex;
            private String Unit;
            private String ArmyGroup;
            private String PhoneNo;
            private String TelNo;
            private String GroupName;
            private String LoginName;
            private String Password;
            private String Authority;
            private String ModifyTime;
            private String RegisterTime;
            private String NoIndex;
            private String AuthenticationNo;
            private String IsAndroid;
            private String BeginNum;
            private String EndNum;

            public String getNo() {
                return No;
            }

            public void setNo(String No) {
                this.No = No;
            }

            public String getName() {
                return Name;
            }

            public void setName(String Name) {
                this.Name = Name;
            }

            public String getCardNo() {
                return CardNo;
            }

            public void setCardNo(String CardNo) {
                this.CardNo = CardNo;
            }

            public String getPosition() {
                return Position;
            }

            public void setPosition(String Position) {
                this.Position = Position;
            }

            public int getSex() {
                return Sex;
            }

            public void setSex(int Sex) {
                this.Sex = Sex;
            }

            public String getUnit() {
                return Unit;
            }

            public void setUnit(String Unit) {
                this.Unit = Unit;
            }

            public String getArmyGroup() {
                return ArmyGroup;
            }

            public void setArmyGroup(String ArmyGroup) {
                this.ArmyGroup = ArmyGroup;
            }

            public String getPhoneNo() {
                return PhoneNo;
            }

            public void setPhoneNo(String PhoneNo) {
                this.PhoneNo = PhoneNo;
            }

            public String getTelNo() {
                return TelNo;
            }

            public void setTelNo(String TelNo) {
                this.TelNo = TelNo;
            }

            public String getGroupName() {
                return GroupName;
            }

            public void setGroupName(String GroupName) {
                this.GroupName = GroupName;
            }

            public String getLoginName() {
                return LoginName;
            }

            public void setLoginName(String LoginName) {
                this.LoginName = LoginName;
            }

            public String getPassword() {
                return Password;
            }

            public void setPassword(String Password) {
                this.Password = Password;
            }

            public String getAuthority() {
                return Authority;
            }

            public void setAuthority(String Authority) {
                this.Authority = Authority;
            }

            public String getModifyTime() {
                return ModifyTime;
            }

            public void setModifyTime(String ModifyTime) {
                this.ModifyTime = ModifyTime;
            }

            public String getRegisterTime() {
                return RegisterTime;
            }

            public void setRegisterTime(String RegisterTime) {
                this.RegisterTime = RegisterTime;
            }

            public String getNoIndex() {
                return NoIndex;
            }

            public void setNoIndex(String NoIndex) {
                this.NoIndex = NoIndex;
            }

            public String getAuthenticationNo() {
                return AuthenticationNo;
            }

            public void setAuthenticationNo(String AuthenticationNo) {
                this.AuthenticationNo = AuthenticationNo;
            }

            public String getIsAndroid() {
                return IsAndroid;
            }

            public void setIsAndroid(String IsAndroid) {
                this.IsAndroid = IsAndroid;
            }

            public String getBeginNum() {
                return BeginNum;
            }

            public void setBeginNum(String BeginNum) {
                this.BeginNum = BeginNum;
            }

            public String getEndNum() {
                return EndNum;
            }

            public void setEndNum(String EndNum) {
                this.EndNum = EndNum;
            }
        }

        public static class PeopleLeaveRrdBean {
            /**
             * No : 申请人
             * Result :
             * CurrentApproveNo :
             * Approver1No : 审批人1
             * Approver2No : 审批人2
             * Approver3No : 审批人3
             * Approver4No : 审批人4
             * Approver5No : 审批人5
             * RegisterTime : 申请时间
             * OutTime : 预计外出时间
             * InTime : 预计归来时间
             * Content : 请假原因
             * ActualOutTime : 实际外出时间
             * ActualInTime : 实际归来时间
             * ModifyTime : 最后修改时间
             * MultiLevelResult : 审批结果
             * Process : 审批状态
             * LevelNum : 审批级数
             * bCancel : 是否取消请假
             * bFillup : 是否后补请假
             * NoIndex : 序号
             * BeginNum :
             * EndNum :
             * AuthenticationNo :
             * IsAndroid :
             */

            private String No;
            private String Result;
            private String CurrentApproveNo;
            private String Approver1No;
            private String Approver2No;
            private String Approver3No;
            private String Approver4No;
            private String Approver5No;
            private String RegisterTime;
            private String OutTime;
            private String InTime;
            private String Content;
            private String ActualOutTime;
            private String ActualInTime;
            private String ModifyTime;
            private String MultiLevelResult;
            private String Process;
            private String LevelNum;
            private String bCancel;
            private String bFillup;
            private String NoIndex;
            private String BeginNum;
            private String EndNum;
            private String AuthenticationNo;
            private String IsAndroid;

            public String getNo() {
                return No;
            }

            public void setNo(String No) {
                this.No = No;
            }

            public String getResult() {
                return Result;
            }

            public void setResult(String Result) {
                this.Result = Result;
            }

            public String getCurrentApproveNo() {
                return CurrentApproveNo;
            }

            public void setCurrentApproveNo(String CurrentApproveNo) {
                this.CurrentApproveNo = CurrentApproveNo;
            }

            public String getApprover1No() {
                return Approver1No;
            }

            public void setApprover1No(String Approver1No) {
                this.Approver1No = Approver1No;
            }

            public String getApprover2No() {
                return Approver2No;
            }

            public void setApprover2No(String Approver2No) {
                this.Approver2No = Approver2No;
            }

            public String getApprover3No() {
                return Approver3No;
            }

            public void setApprover3No(String Approver3No) {
                this.Approver3No = Approver3No;
            }

            public String getApprover4No() {
                return Approver4No;
            }

            public void setApprover4No(String Approver4No) {
                this.Approver4No = Approver4No;
            }

            public String getApprover5No() {
                return Approver5No;
            }

            public void setApprover5No(String Approver5No) {
                this.Approver5No = Approver5No;
            }

            public String getRegisterTime() {
                return RegisterTime;
            }

            public void setRegisterTime(String RegisterTime) {
                this.RegisterTime = RegisterTime;
            }

            public String getOutTime() {
                return OutTime;
            }

            public void setOutTime(String OutTime) {
                this.OutTime = OutTime;
            }

            public String getInTime() {
                return InTime;
            }

            public void setInTime(String InTime) {
                this.InTime = InTime;
            }

            public String getContent() {
                return Content;
            }

            public void setContent(String Content) {
                this.Content = Content;
            }

            public String getActualOutTime() {
                return ActualOutTime;
            }

            public void setActualOutTime(String ActualOutTime) {
                this.ActualOutTime = ActualOutTime;
            }

            public String getActualInTime() {
                return ActualInTime;
            }

            public void setActualInTime(String ActualInTime) {
                this.ActualInTime = ActualInTime;
            }

            public String getModifyTime() {
                return ModifyTime;
            }

            public void setModifyTime(String ModifyTime) {
                this.ModifyTime = ModifyTime;
            }

            public String getMultiLevelResult() {
                return MultiLevelResult;
            }

            public void setMultiLevelResult(String MultiLevelResult) {
                this.MultiLevelResult = MultiLevelResult;
            }

            public String getProcess() {
                return Process;
            }

            public void setProcess(String Process) {
                this.Process = Process;
            }

            public String getLevelNum() {
                return LevelNum;
            }

            public void setLevelNum(String LevelNum) {
                this.LevelNum = LevelNum;
            }

            public String getBCancel() {
                return bCancel;
            }

            public void setBCancel(String bCancel) {
                this.bCancel = bCancel;
            }

            public String getBFillup() {
                return bFillup;
            }

            public void setBFillup(String bFillup) {
                this.bFillup = bFillup;
            }

            public String getNoIndex() {
                return NoIndex;
            }

            public void setNoIndex(String NoIndex) {
                this.NoIndex = NoIndex;
            }

            public String getBeginNum() {
                return BeginNum;
            }

            public void setBeginNum(String BeginNum) {
                this.BeginNum = BeginNum;
            }

            public String getEndNum() {
                return EndNum;
            }

            public void setEndNum(String EndNum) {
                this.EndNum = EndNum;
            }

            public String getAuthenticationNo() {
                return AuthenticationNo;
            }

            public void setAuthenticationNo(String AuthenticationNo) {
                this.AuthenticationNo = AuthenticationNo;
            }

            public String getIsAndroid() {
                return IsAndroid;
            }

            public void setIsAndroid(String IsAndroid) {
                this.IsAndroid = IsAndroid;
            }
        }
    }
}
