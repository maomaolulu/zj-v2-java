package may.yuntian.sys.utils;

import com.github.pagehelper.PageHelper;

/**
 * @author mi
 */
public class PageUtil2 {
    public static void startPage(){

         Integer pageIndex = ServletUtils.getParameterToInt("pageIndex");
         Integer pageSize = ServletUtils.getParameterToInt("pageSize");
         String orderByColumn = ServletUtils.getParameter("sort");

        PageHelper.startPage(pageIndex,pageSize,orderByColumn );
    }

}
