package com.sqx.modules.orders.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.*;
import com.sqx.modules.app.service.*;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.course.dao.CourseDao;
import com.sqx.modules.course.dao.CourseUserDao;
import com.sqx.modules.course.entity.Course;
import com.sqx.modules.course.entity.CourseDetails;
import com.sqx.modules.course.entity.CourseUser;
import com.sqx.modules.course.service.CourseDetailsService;
import com.sqx.modules.course.service.CourseUserService;
import com.sqx.modules.invite.dao.InviteMoneyDao;
import com.sqx.modules.invite.entity.InviteMoney;
import com.sqx.modules.invite.service.InviteService;
import com.sqx.modules.orders.dao.OrdersDao;
import com.sqx.modules.orders.entity.Orders;
import com.sqx.modules.orders.entity.PayRequest;
import com.sqx.modules.orders.service.OrdersService;
import com.sqx.modules.pay.controller.app.AliPayController;
import com.sqx.modules.pay.service.WxService;
import com.sqx.modules.utils.AliPayOrderUtil;
import com.sqx.modules.wechat.request.CurrencyPayRequest;
import com.sqx.modules.wechat.service.WechatVirtualPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
@Slf4j
public class OrdersServiceImpl extends ServiceImpl<OrdersDao, Orders> implements OrdersService {
    @Autowired
    private WxService wxService;
    @Autowired
    private AliPayController aliPayController;
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private CourseUserDao courseUserDao;
    @Autowired
    private UserVipService userVipService;
    @Autowired
    private CourseUserService courseUserService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private VipDetailsService vipDetailsService;
    @Autowired
    private CourseDetailsService courseDetailsService;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private UserService userService;
    @Autowired
    private InviteService inviteService;
    @Autowired
    private InviteMoneyDao inviteMoneyDao;

    private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);


    @Override
    public Orders selectOrdersByCourseIdAndUserId(Long userId, Long courseId) {
        return baseMapper.selectOrdersByCourseIdAndUserId(userId, courseId);
    }


    @Override
    public Result insertOrders(Orders orders) {
        //如果订单的种类是短剧
        if (orders.getOrdersType() == 1) {
            //将短剧加入到我的列表
            CourseUser courseUser = new CourseUser();
            //设置短剧id
            courseUser.setCourseId(orders.getCourseId());
            courseUser.setCourseDetailsId(orders.getCourseDetailsId());
            if (courseUser.getCourseDetailsId() != null) {
                courseUser.setClassify(2);
            } else {
                courseUser.setClassify(1);
            }
            //设置用户id
            courseUser.setUserId(orders.getUserId());
            //设置订单id
            courseUser.setOrderId(orders.getOrdersId());
            //加入我的列表
            courseUserService.insertCourseUser(courseUser);
            return Result.success("短剧订单处理完成！");
        } else {
            //订单的种类是会员
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //查询用户是否成为过会员
            UserVip userVip = userVipService.selectUserVipByUserId(orders.getUserId());
            if (userVip != null) {
                //日历
                Calendar cal = Calendar.getInstance();
                //会员没有到期
                if (userVip.getIsVip().equals(2)) {
                    try {
                        //将会员到期时间设置在日历表中
                        cal.setTime(sdf.parse(userVip.getEndTime()));
                        //判断续费会员的时间
                        if (orders.getVipNameType() == 0) {
                            //一个月
                            cal.add(Calendar.MONTH, 1);
                        } else if (orders.getVipNameType() == 1) {
                            //一季度
                            cal.add(Calendar.MONTH, 3);
                        } else if (orders.getVipNameType() == 2) {
                            //一年
                            cal.add(Calendar.YEAR, 1);
                        }
                        userVip.setIsVip(2);
                        //更新会员的开通时间
                        userVip.setCreateTime(sdf.format(new Date()));
                        //更新会员的到期时间
                        userVip.setEndTime(sdf.format(cal.getTime()));
                        //更新会员信息
                        userVipService.updateById(userVip);
                        return Result.success().put("data", "未到期会员续费成功！");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    //会员到期了
                    //将现在的时间设置在日历中
                    cal.setTime(new Date());
                    //判断续费会员的时间
                    if (orders.getVipNameType() == 0) {
                        //一个月
                        cal.add(Calendar.MONTH, 1);
                    } else if (orders.getVipNameType() == 1) {
                        //一季度
                        cal.add(Calendar.MONTH, 3);
                    } else if (orders.getVipNameType() == 2) {
                        //-年
                        cal.add(Calendar.YEAR, 1);
                    }
                    userVip.setIsVip(2);
                    //更新会员的开通时间
                    userVip.setCreateTime(sdf.format(new Date()));
                    //更新会员的到期时间
                    userVip.setEndTime(sdf.format(cal.getTime()));
                    //更新会员信息
                    userVipService.updateById(userVip);
                    return Result.success().put("data", "到期会员续费成功！");
                }
            } else {
                //不是会员开通会员
                //创建会员对象
                userVip = new UserVip();
                //设置开通的用户id
                userVip.setUserId(orders.getUserId());
                //设置开通时间
                userVip.setCreateTime(sdf.format(new Date()));
                //日历
                Calendar cal = Calendar.getInstance();
                //判断开通的会员类型
                if (orders.getVipNameType() == 0) {
                    //-月
                    cal.add(Calendar.MONTH, 1);
                } else if (orders.getVipNameType() == 1) {
                    //一季
                    cal.add(Calendar.MONTH, 3);
                } else if (orders.getVipNameType() == 2) {
                    //一年
                    cal.add(Calendar.YEAR, 1);
                }
                userVip.setIsVip(2);
                //设置到期时间
                userVip.setEndTime(sdf.format(cal.getTime()));
                //注册会员信息
                userVipService.save(userVip);
                return Result.success().put("data", "到期会员续费成功！");
            }
        }
        return Result.success("订单处理完成！");
    }

    /**
     * 生成商品订单信息
     *
     * @param courseId
     * @param userId
     * @return
     */
    @Override
    public Result insertCourseOrders(Long courseId, Long courseDetailsId, Long userId) {
        log.info("生成商品订单信息接口入参为：{},{}", courseId, userId);
        reentrantReadWriteLock.writeLock().lock();
        try {
            /*CourseUser courseUser1 = courseUserDao.selectCourseUser(courseId, userId);
            if(courseUser1!=null){
                return Result.error("您已经购买过了，请不要重复点击！");
            }*/
            //返回的类型
            Map<String, Object> result = new HashMap<>();
            //查询会员信息
            UserVip userVip = userVipService.selectUserVipByUserId(userId);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //订单模板对象
            Orders orders = new Orders();
            if (courseId != null) {
                //根据短剧id去查询短剧相关信息 来填充订单模板
                QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("is_delete", 0);
                //短剧必须是未删除的
                queryWrapper.eq("course_id", courseId);
                Course course = courseDao.selectOne(queryWrapper);
                if (course == null) {
                    return Result.error("系统繁忙，请刷新后重试！");
                }
                //设置订单编号
                orders.setOrdersNo(AliPayOrderUtil.createOrderId());
                //设置用户id
                orders.setUserId(userId);
                //设置短剧id
                orders.setCourseId(courseId);
                orders.setCourseDetailsId(courseDetailsId);
                if (courseDetailsId != null) {
                    CourseDetails courseDetails = courseDetailsService.getById(courseDetailsId);
                    orders.setPayMoney(courseDetails.getPrice());
                } else {
                    orders.setPayMoney(course.getPrice());
                }
                //设置支付状态
                orders.setStatus(0);
                //设置订单创建时间
                orders.setCreateTime(df.format(new Date()));
                //设置订单种类
                orders.setOrdersType(1);

                //不是会员或会员过期直接生成订单直接生成订单
                int count = baseMapper.insertOrders(orders);
                result.put("flag", 2);
                result.put("orders", orders);
                if (count > 0) {
                    return Result.success("生成订单成功！").put("data", result);
                } else {
                    return Result.error("生成订单失败！");
                }
            } else {
                return Result.error("短剧的id不能为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("生成商品订单错误！！！" + e.getMessage());
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return Result.error("系统繁忙，请稍后再试！");
    }

    /**
     * 生成会员订单
     *
     * @param vipDetailsId 会员详情id
     * @param userId
     * @return
     */
    @Override
    public Result insertVipOrders(Long vipDetailsId, Long userId) {
        VipDetails vipDetails = vipDetailsService.getById(vipDetailsId);
        //创建订单返回对象
        Orders orders = new Orders();
        //设置订单编号
        orders.setOrdersNo(AliPayOrderUtil.createOrderId());
        //设置支付金额
        orders.setPayMoney(vipDetails.getMoney());
        //设置订单类型
        orders.setOrdersType(2);
        //设置要开通的会员类型
        orders.setVipNameType(vipDetails.getVipNameType());
        //设置支付状态
        orders.setStatus(0);
        //设置用户id
        orders.setUserId(userId);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //设置创建时间
        orders.setCreateTime(df.format(new Date()));
        //插入到订单表中
        baseMapper.insertOrders(orders);
        return Result.success().put("data", orders);
    }


    @Override
    public Result payMoney(Long ordersId) {
        reentrantReadWriteLock.writeLock().lock();
        try{
            Orders orders = baseMapper.selectById(ordersId);
            if (orders == null || !orders.getStatus().equals(0)) {
                return Result.error("订单错误，请刷新后重试！");
            }
            UserMoney userMoney = userMoneyService.selectUserMoneyByUserId(orders.getUserId());
            InviteMoney inviteMoney = inviteMoneyDao.selectInviteMoneyByUserId(orders.getUserId());
            if (userMoney.getMoney().doubleValue() >= orders.getPayMoney().doubleValue()) {
                UserEntity userEntity = userService.selectUserById(orders.getUserId());
                userMoneyService.updateMoney(2, orders.getUserId(), orders.getPayMoney().doubleValue());
                UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                userMoneyDetails.setMoney(orders.getPayMoney());
                userMoneyDetails.setUserId(orders.getUserId());
                userMoneyDetails.setContent("看点支付订单");
                userMoneyDetails.setTitle("下单成功，订单号：" + orders.getOrdersNo());
                userMoneyDetails.setType(2);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                userMoneyDetails.setCreateTime(simpleDateFormat.format(new Date()));
                userMoneyDetailsService.save(userMoneyDetails);
                orders.setPayWay(6);
                orders.setStatus(1);
                baseMapper.updateById(orders);

                UserEntity byUser = userService.queryByInvitationCode(userEntity.getInviterCode());
                inviteService.updateInvite(byUser, DateUtils.format(new Date()), userEntity.getUserId(), orders.getPayMoney());
                insertOrders(orders);
                return Result.success();
            } else {
                BigDecimal add = BigDecimal.valueOf(inviteMoney.getMoney()).add(userMoney.getMoney());
                if (add.doubleValue() < orders.getPayMoney().doubleValue()) {
                    return Result.error("账户不足，请充值！");
                }
                UserEntity userEntity = userService.selectUserById(orders.getUserId());
                userMoneyService.updateMoney(2, orders.getUserId(), userMoney.getMoney().doubleValue());
                UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                userMoneyDetails.setMoney(orders.getPayMoney());
                userMoneyDetails.setUserId(orders.getUserId());
                userMoneyDetails.setContent("看点支付订单");
                userMoneyDetails.setTitle("下单成功，订单号：" + orders.getOrdersNo());
                userMoneyDetails.setType(2);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                userMoneyDetails.setCreateTime(simpleDateFormat.format(new Date()));
                userMoneyDetailsService.save(userMoneyDetails);
                BigDecimal subtract = orders.getPayMoney().subtract(userMoney.getMoney());
                inviteMoneyDao.updateInviteMoneySumSub(subtract.doubleValue(), orders.getUserId());
                orders.setPayWay(6);
                orders.setStatus(1);
                baseMapper.updateById(orders);

                UserEntity byUser = userService.queryByInvitationCode(userEntity.getInviterCode());
                inviteService.updateInvite(byUser, DateUtils.format(new Date()), userEntity.getUserId(), orders.getPayMoney());
                insertOrders(orders);
                return Result.success();
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("看点支付订单异常："+e.getMessage(),e);
        }finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return Result.success();
    }

//    @Override
//    public Result payMoney(PayRequest payRequest) {
//        if (StrUtil.isBlank(payRequest.getPlatform())) {
//            // 如果不是小程序支付，走原有逻辑
//            return payMoney(payRequest.getOrderId());
//        }
//
//        if ("wechat".equals(payRequest.getPlatform())) {
//            Orders orders = baseMapper.selectById(payRequest.getOrderId());
//            Result result = payMoney(payRequest.getOrderId());
//            if (0 == (int) result.get("code")) {
//                // 0 即为支付成功，此处扣除用户微信虚拟代币
//                wechatVirtualPaymentService.init(payRequest.getTenant());
//
//                // 想一下这里怎么做。 苹果登陆充值后，没有代币，安卓登陆后，扣款怎么办。
//                wechatVirtualPaymentService.pay(new CurrencyPayRequest());
//            }
//            return result;
//        }
//
//        return Result.success();
//    }

    @Override
    public Result refundOrder(Long ordersId, String refundContent) {
        Orders bean = baseMapper.selectById(ordersId);
        if (!bean.getStatus().equals(1)) {
            return Result.error("订单未支付或已退款！");
        }
        bean.setRefundContent(refundContent);
        if (bean.getPayWay() == 1 || bean.getPayWay() == 2 || bean.getPayWay() == 3) {
            boolean refund = wxService.refund(bean);
            if (!refund) {
                return Result.error("退款失败！");
            }
        } else if(bean.getPayWay()==4){
            String code = aliPayController.alipayRefund(bean);
            if (StringUtils.isNotBlank(code)) {
                JSONObject jsonObject = JSON.parseObject(code);
                JSONObject alipay_trade_refund_response = jsonObject.getJSONObject("alipay_trade_refund_response");
                String code1 = alipay_trade_refund_response.getString("code");
                if (!"10000".equals(code1)) {
                    return Result.error("退款失败！");
                }
            } else {
                return Result.error("退款失败！");
            }
        }else{
            userMoneyService.updateMoney(1,bean.getUserId(),bean.getPayMoney().doubleValue());
            UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
            userMoneyDetails.setMoney(bean.getPayMoney());
            userMoneyDetails.setUserId(bean.getUserId());
            userMoneyDetails.setContent("订单:"+bean.getOrdersNo());
            userMoneyDetails.setTitle("订单退款："+bean.getPayMoney());
            userMoneyDetails.setType(1);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            userMoneyDetails.setCreateTime(simpleDateFormat.format(new Date()));
            userMoneyDetailsService.save(userMoneyDetails);
        }
        bean.setStatus(2);
        baseMapper.updateById(bean);
        if (bean.getOrdersType() == 1) {
            CourseUser courseUser = courseUserDao.selectOne(new QueryWrapper<CourseUser>().eq("order_id", bean.getOrdersId()));
            if (courseUser != null) {
                courseUserDao.deleteById(courseUser.getCourseUserId());
            }
        } else {
            UserVip userVip = userVipService.selectUserVipByUserId(bean.getUserId());
            if (userVip != null) {
                userVipService.removeById(userVip.getVipId());
            }
            courseUserDao.deleteCourseUserByVipUser(bean.getUserId());
        }
        return Result.success("退款成功！");
    }

    @Override
    public Result selectOrders(Integer page, Integer limit, String ordersNo, Integer status, Long userId,Long courseId,Integer flag,String time) {
        Page<Orders> pages = new Page<>(page, limit);
        return Result.success().put("data", new PageUtils(baseMapper.selectOrdersByOrdersNo(pages, ordersNo, status, userId,courseId,flag,time)));
    }

    @Override
    public Result selectOrderByUserId(Integer page, Integer limit, Long userId) {
        IPage<Orders> orderPage = new Page<>(page, limit);
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("orders_type", 1);
        queryWrapper.eq("status", 1);
        queryWrapper.orderByDesc("create_time");
        IPage<Orders> iPage = baseMapper.selectPage(orderPage, queryWrapper);
        List<Orders> bean = iPage.getRecords();
        if (bean != null && bean.size() > 0) {
            for (int i = 0; bean.size() > i; i++) {
                bean.get(i).setCourse(courseDao.selectById(bean.get(i).getCourseId()));
            }
        }
        return Result.success().put("data", iPage);
    }

    @Override
    public Result deleteOrders(String ids) {
        String[] split = ids.split(",");
        baseMapper.deleteOrders(split);
        return Result.success();
    }


    @Override
    public Orders selectOrderById(Long orderId) {
        return baseMapper.selectById(orderId);
    }

    @Override
    public Orders selectOrderByOrdersNo(String ordersNo) {
        return baseMapper.selectOne(new QueryWrapper<Orders>().eq("orders_no", ordersNo));
    }

    @Override
    public Double statisticsIncomeMoney(String time, Integer flag, Integer ordersType) {
        return baseMapper.statisticsIncomeMoney(time, flag, ordersType);
    }

    @Override
    public Result selectOrdersMoneyList(Integer page, Integer limit, Integer flag, String time) {
        return Result.success().put("data", baseMapper.selectOrdersMoneyList(new Page<>(page, limit), flag, time));
    }


    @Override
    public Integer selectOrdersCount(Integer status, Integer ordersType, Integer flag, String time) {
        return baseMapper.selectOrdersCount(status, ordersType, flag, time);
    }

    @Override
    public Double selectOrdersMoney(Integer status, Integer ordersType, Integer flag, String time,Long courseId) {
        return baseMapper.selectOrdersMoney(status, ordersType, flag, time,courseId);
    }


}
