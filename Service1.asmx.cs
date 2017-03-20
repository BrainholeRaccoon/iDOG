using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Services;

namespace StockManageWebservice
{
    /// <summary>
    /// Service1 的摘要说明
    /// </summary>
    [WebService(Namespace = "http://tempuri.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    // 若要允许使用 ASP.NET AJAX 从脚本中调用此 Web 服务，请取消对下行的注释。
    //[System.Web.Script.Services.ScriptService]
    public class Service1 : System.Web.Services.WebService
    {
        DBOperation dbOperation = new DBOperation();

        
        [WebMethod(Description = "select all user info")]
        public string[] selectAllUserInfor()
        {
            return dbOperation.selectAllUserInfor().ToArray();
        }

        [WebMethod(Description = "select all health info")]
        public string[] selectAllHealthInfor()
        {
            return dbOperation.selectAllHealthInfor().ToArray();
        }


        [WebMethod(Description = "add")]
        public bool insertUserInfo(string time,string food, string no)
        {
            return dbOperation.insertUserInfo(time,food,no);
        }

        [WebMethod(Description = "delete")]
        public bool deleteUserInfo(string no)
        {
            return dbOperation.deleteUserInfo(no);
        }
        [WebMethod(Description = "update mode")]
        public bool updateUserMode(string no)
        {
            return dbOperation.updateUserMode(no);
        }
    }
}