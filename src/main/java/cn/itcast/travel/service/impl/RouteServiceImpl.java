package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImgDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {
    private RouteDao routeDao = new RouteDaoImpl();
    private RouteImgDao routeImgDao = new RouteImgDaoImpl();
    private SellerDao sellerDao = new SellerDaoImpl();
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();
    /**
     * 分页查询
     * 根据cid,start,pageSize查询当前页的数据集合
     * start为查询结果的索引值(默认从0开始)
     * pageSize为查询结果返回的数量
     *
     * @param cid
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize,String rname) {

        //封装PageBean
        PageBean<Route> pb = new PageBean<Route>();
        //设置当前页码
        pb.setCurrentPage(currentPage);
        //设置每页显示条数
        pb.setPageSize(pageSize);
        //设置总记录数
        int totalCount = routeDao.findTotalCount(cid,rname);
        pb.setTotalCount(totalCount);
        //设置当前页显示的数据集合
        int start = (currentPage - 1) * pageSize;//开始的记录数（已知当前处于第几页，一页显示多少条）  公式：（当前页码 - 1）* 每页显示的记录数
        List<Route> list = routeDao.findByPage(cid, start, pageSize,rname);
        pb.setList(list);
        //设置总页数（总页数 = 总记录数 / 每页显示条数）注意判断能否除得尽，如果除不尽就加1
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        pb.setTotalPage(totalPage);
        return pb;
    }
    /**
     * 根据id查询
     *
     * @param rid
     * @return
     */
    @Override
    public Route findOne(String rid) {
        //1.根据rid去route表中查询route对象
        Route route = routeDao.findOne(Integer.parseInt(rid));
        //2.根据routed的rid 查询图片集合信息
        List<RouteImg> routeImgList = routeImgDao.findByRid(Integer.parseInt(rid));
        //3.将集合设置到route对象中
        route.setRouteImgList(routeImgList);
        //4.根据route的sid查询卖家信息
        Seller seller = sellerDao.findById(route.getSid());
        route.setSeller(seller);

        //5.查询收藏次数
        int count = favoriteDao.findCountByRid(route.getRid());
        route.setCount(count);


        return route;
    }

}


