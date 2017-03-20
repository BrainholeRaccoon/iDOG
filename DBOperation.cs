using System;
using System.Data;
using System.Configuration;
using System.Linq;
using System.Web;
using System.Web.Security;
using System.Web.UI;
using System.Web.UI.HtmlControls;
using System.Web.UI.WebControls;
using System.Web.UI.WebControls.WebParts;
using System.Xml.Linq;
using System.Data.SqlClient;
using System.Text.RegularExpressions;
using System.Collections;
using System.Collections.Generic;

namespace StockManageWebservice
{
    public class DBOperation:IDisposable
    {
        public static SqlConnection sqlCon; 
     
        private String ConServerStr = "Data Source=YINCHUAN-Y500;Initial Catalog=acc;Persist Security Info=True;User ID=sa;Password=123456";
     
        public DBOperation()
        {
            if (sqlCon == null)
            {
                sqlCon = new SqlConnection();
                sqlCon.ConnectionString = ConServerStr;
                sqlCon.Open();
            }
        }

        public void Dispose()
        {
            if (sqlCon != null)
            {
                sqlCon.Close();
                sqlCon = null;
            }
        }
  
        //
        public List<string> selectAllUserInfor()
        {
            List<string> list = new List<string>();
            try
            {
                
                string sql = "select * from zsf";
                SqlCommand cmd = new SqlCommand(sql,sqlCon);
                SqlDataReader reader = cmd.ExecuteReader();
                while (reader.Read())
                {
                    list.Add(reader[0].ToString());
                    list.Add(reader[1].ToString());
                    list.Add(reader[2].ToString());

                }
                reader.Close();
                cmd.Dispose();
            }
            catch(Exception)
            {
                     
            }
            return list;
        }

        public List<string> selectAllHealthInfor()
        {
            List<string> list = new List<string>();
            try
            {

                string sql = "select * from zsfdog";
                SqlCommand cmd = new SqlCommand(sql, sqlCon);
                SqlDataReader reader = cmd.ExecuteReader();
                while (reader.Read())
                {
                    list.Add(reader[0].ToString());
                    list.Add(reader[1].ToString());
                    list.Add(reader[2].ToString());

                }
                reader.Close();
                cmd.Dispose();
            }
            catch (Exception)
            {

            }
            return list;
        }
        //
        public bool insertUserInfo(string time,string food, string no)
        {
            try
            {
                
                string sql = "insert into zsf (time,food,no) values ('" + time + "','" + food + "','" + no + "')";
                SqlCommand cmd = new SqlCommand(sql, sqlCon);
                cmd.ExecuteNonQuery();
                cmd.Dispose();
                return true;
            }
            catch (Exception)
            {
                return false;
            }
        }

        //
        public bool deleteUserInfo(string no)
        {
            try
            {
                
                string sql = "delete from zsf where no=" + no;
                SqlCommand cmd = new SqlCommand(sql, sqlCon);
                cmd.ExecuteNonQuery();
                cmd.Dispose();
                return true;
            }
            catch (Exception)
            {
                return false;
            }
        }
        //
        public bool updateUserMode(string no)
        {
            try
            {

                string sql = "update zsfmode set mode ="+no;
                SqlCommand cmd = new SqlCommand(sql, sqlCon);
                cmd.ExecuteNonQuery();
                cmd.Dispose();
                return true;
            }
            catch (Exception)
            {
                return false;
            }
        }
    }
}