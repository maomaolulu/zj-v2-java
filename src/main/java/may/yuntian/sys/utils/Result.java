package may.yuntian.sys.utils;

import com.github.pagehelper.PageInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Result extends HashMap<String, Object> {
    //
    private static final long serialVersionUID = -8157613083634272196L;

    public Result() {
        put("code", 200);
        put("msg", "success");
    }

    public static Result error() {
        return error(500, "未知异常，请联系管理员");
    }

    public static Result error(String msg) {
        return error(500, msg);
    }

    public static Result error(int code, String msg) {
        Result r = new Result();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static Result error(int code, String msg, Object obj) {
        Result r = new Result();
        r.put("code", code);
        r.put("msg", msg);
        r.put("data", obj);
        return r;
    }

    public static Result ok(String msg) {
        Result r = new Result();
        r.put("msg", msg);
        r.put("code", 200);
        return r;
    }

    public static Result data(Object obj) {
        Result r = new Result();
        r.put("code", 200);
        r.put("data", obj);
        return r;
    }
    public static Result ok(String msg, Object obj) {
        Result r = new Result();
        r.put("msg", msg);
        r.put("code", 200);
        r.put("data", obj);
        return r;
    }

    public static Result ok(Map<String, Object> map) {
        Result r = new Result();
        r.putAll(map);
        return r;
    }

    public static Result ok() {
        return new Result();
    }

    @Override
    public Result put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public static Result resultData(List<?> list) {
        PageInfo<?> pageInfo = new PageInfo(list);
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("list", list);
        m.put("currPage", pageInfo.getPageNum());
        m.put("pageSize", pageInfo.getPageSize());
        m.put("totalCount", pageInfo.getTotal());
        m.put("totalPage", pageInfo.getPages());
        return Result.data(m);
    }

}
