<template>
	<div>
		<div style="display: inline-block;font-size:18px;margin-bottom: 15px;">
			<a href="#" @click="prev" style="text-decoration:none;font-size: 14px;">
				<icon-svg name="jiantou" style="width: 1.2em;height: 1.2em;position: relative;top: 0.3em;"></icon-svg>
				返回
			</a>
			<span style="display: inline-block;margin: 0 15px;color: #D9D9D9;">|</span>
			<span>用户详情</span>
		</div>
		<el-tabs v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="用户详情" name="first">
				<div class="detailtabel">
					<div class="table_main">
						<table>
							<tbody>
								<tr>
									<th>本月充值金额</th>
									<td>{{ tablenum.consume ? `${tablenum.consume}元` : '0元' }}</td>
									<th>本月提现金额</th>
									<td>{{ tablenum.income ? `${tablenum.income}元` : '0元' }}</td>
									<th>本月邀请数量</th>
									<td class="border-rt">{{ tablenum.count ? `${tablenum.count}人` : '0人' }}
									</td>
								</tr>
								<tr>
									<th>本月派单数量</th>
									<td>{{ tablenum.helpTakeCount ? `${tablenum.helpTakeCount}笔` : '0笔' }}</td>
									<th>积分</th>
									<td>{{ jifen.integralNum ? `${jifen.integralNum}` : '0' }}
									<el-button size="mini" :disabled="!isAuth('userList:updateJf')"
										style="color: #4f9dec;background: #fff;border: none;" @click="rechargenoneJf(tableData.userId)">
										修改</el-button>
									</td>
									<th>openId</th>
									<td class="border-rt">{{ tableData.openId ? tableData.openId : '未绑定' }}</td>
								</tr>
								<tr>
									<th>邀请人邀请码</th>
									<td>{{ tableData.invitationCode }}</td>
									<th>收益</th>
									<td style="color: rgb(245, 108, 108)">
										{{ tablemoney.money ? `${tablemoney.money}元` : '0元' }}

									</td>
									<th>钱包</th>
									<td class="border-rt">
										{{qianbao.money}}
										<el-button size="mini" :disabled="!isAuth('userList:updateQb')"
											style="color: #4f9dec;background: #fff;border: none;" @click="rechargenone(tableData.userId)">
											修改</el-button>

									</td>

								</tr>
								<tr>
									<th>创建时间</th>
									<td>{{ tableData.createTime }}</td>
									<th>手机号</th>
									<td>{{ tableData.phone ? tableData.phone : '未绑定' }}
									<el-button size="mini" :disabled="!isAuth('userList:update')"
										style="color: #4f9dec;background: #fff;border: none;" @click="ageChangeU(tableData)">
										修改</el-button>
									</td>
									<th>图像</th>
									<td class="border-rt">
										<img
											:src="tableData.avatar ? tableData.avatar : 'https://your-admin-domain.com/sqx_fast/logo.png'"
											width="80" height="80" />
										<el-button size="mini" :disabled="!isAuth('userList:update')"
											style="color: #4f9dec;background: #fff;border: none;" @click="ageChangeU(tableData)">
											修改</el-button>
									</td>
								</tr>
								<tr>
									<th>微信名称</th>
									<td>{{ tableData.userName }}
										<el-button size="mini" :disabled="!isAuth('userList:update')"
											style="color: #4f9dec;background: #fff;border: none;" @click="ageChangeU(tableData)">
											修改</el-button>
									</td>
									<th>来源</th>
									<td>{{ tableData.platform }}</td>
									<th>支付宝姓名</th>
									<td class="border-rt">{{ tableData.zhifubaoName ? tableData.zhifubaoName : '未绑定' }}
									</td>
								</tr>
								<tr>
									<th>是否是会员</th>
									<td>
										{{ tableData.member==2?'是':'否' }}
										<el-button size="mini" type="warning" @click="updateVip(tableData)"
											:disabled="!isAuth('userList:updateVip')" v-if="tableData.member!=2">设置会员</el-button>
										<el-button size="mini" type="warning" @click="quxiaoVip(tableData)"
											:disabled="!isAuth('userList:updateVip')" v-else plain>取消会员
										</el-button>
									</td>
									<th>推广收益比例</th>
									<td>
										{{ tableData.rate }}
										<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
											style="color: #4f9dec;background: #fff;border: none;" @click="xiugai(tableData,'rate')">
											修改</el-button>
										</span>
									</td>
									<th>编号</th>
									<td class="border-rt">
										{{ tableData.userId }}
									</td>
								</tr>
								<tr>
									<th class="border-bt">邀请码</th>
									<td class="border-bt">{{ tableData.inviterCode }}</td>
									<th class="border-bt">支付宝账号</th>
									<td class="border-bt">{{ tableData.zhifubao ? tableData.zhifubao : '未绑定' }}</td>
									<th class="border-bt">用户状态</th>
									<td class="border-bt border-rt">
										<span v-if="tableData.state == 1 ">正常</span>
										<span v-if="tableData.state == 2 " style="color: #f56c6c;">禁用</span>
										<span style="color: #4f9dec;cursor:pointer;"
											@click="stateChange(tableData.userId)">更改状态</span>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</el-tab-pane>
			<!-- <el-tab-pane label="发布信息" name="third">
				<div>
					<div style="position: relative;display: inline-block;">
						<span>标题名称：</span>
						<el-input style="width: 200px;" @keydown.enter.native="phoneSelect" placeholder="请输入标题名称"
							v-model="search">
						</el-input>&nbsp;&nbsp;
					</div>
					<el-button style="margin:10px;" size="mini" type="primary" icon="document" @click="phoneSelect">查询
					</el-button>
					<el-button style="margin:10px;" size="mini" type="primary" icon="document" @click="cleans2">重置
					</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="userData.records">
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="titleImg" label="封面图">
						<template slot-scope="scope">
							　　<img :src="scope.row.titleImg" width="40" height="40" />
						</template>
					</el-table-column>
					<el-table-column prop="name" label="名称" width="150">
					</el-table-column>
					<el-table-column prop="house" label="户型" width="100">
					</el-table-column>
					<el-table-column prop="area" label="大小" width="100">
					</el-table-column>
					<el-table-column prop="price" label="价格" width="100">
					</el-table-column>
					<el-table-column prop="address" label="地址" width="200">
					</el-table-column>
					<el-table-column prop="remark" label="详情" width="200">
					</el-table-column>
					<el-table-column prop="titleImg" label="详情图" width="180">
						<template slot-scope="scope">
							　　<img v-for="(item,index) in scope.row.imgs" :key="index" :src="item" width="40"
								height="40" @click="refund(scope.row)" />
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="联系电话" width="140">
					</el-table-column>
					<el-table-column prop="contactNum" label="联系次数" width="100">
					</el-table-column>
					<el-table-column prop="classifyId" label="分类">
					</el-table-column>
					<el-table-column prop="status" label="状态" width="100">
						<template slot-scope="scope">
							<span style="color: #4f9dec;" v-if="scope.row.status === 0 ">待审核</span>
							<span style="color: #4f9dec;" v-if="scope.row.status === 1 ">上架</span>
							<span style="color: #4f9dec;" v-if="scope.row.status === 2 ">下架</span>
							<span style="color: #4f9dec;" v-if="scope.row.status === 3 ">驳回</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="180">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
						:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="userData.total">
					</el-pagination>
				</div>
			</el-tab-pane> -->
			<el-tab-pane label="提现记录" name="sixth">
				<el-table v-loading="tableDataLoading" :data="withdrawData.list">
					<el-table-column prop="id" label="编号" width="80"></el-table-column>
					<el-table-column prop="zhifubao" label="支付宝账号"></el-table-column>
					<el-table-column prop="zhifubaoName" label="支付宝名称"></el-table-column>
					<el-table-column prop="money" label="提现金额">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{ scope.row.money }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="orderNumber" label="订单编号"></el-table-column>
					<el-table-column prop="outAt" label="转账时间"></el-table-column>
					<el-table-column prop="createAt" label="创建时间"></el-table-column>
					<el-table-column fixed="right" prop="state" label="状态" width="100">
						<template slot-scope="scope">
							<span style="color: #4f9dec;" v-if="scope.row.state === -1 ">已退款</span>
							<span style="color: #4f9dec;" v-if="scope.row.state === 0 ">待转账</span>
							<span style="color: #4f9dec;" v-if="scope.row.state === 1 ">已到账</span>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="withdrawData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="钱包明细" name="eighth">
				<el-table v-loading="tableDataLoading" :data="walletData.records">
					<el-table-column prop="id" label="编号" width="100"></el-table-column>
					<el-table-column prop="title" label="标题" width="250"></el-table-column>
					<el-table-column prop="content" label="内容"></el-table-column>
					<el-table-column prop="money" label="金额" width="100">
						<template slot-scope="scope">
							<span style="color: #009900;" v-if="scope.row.type==1">+ {{ scope.row.money }}</span>
							<span style="color: #f56c6c;" v-else>- {{ scope.row.money }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="walletData.total">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="充值明细" name="seventh">
				<div style="display: inline-block;">
					<span>开始时间：</span>
					<el-date-picker style="width: 160px;margin-left: 10px;" v-model="startTime" align="right"
						type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
					</el-date-picker>&nbsp;&nbsp;&nbsp;
					<span>截止时间：</span>
					<el-date-picker style="width: 160px;margin-left: 10px;" v-model="endTime" align="right"
						type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
					</el-date-picker>
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="timeDate2">
						查询</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="rechargeData.list">
					<el-table-column prop="id" label="编号" width="50"></el-table-column>
					<el-table-column prop="orderId" label="充值订单号" width="200"></el-table-column>
					<el-table-column prop="money" label="充值金额"></el-table-column>
					<el-table-column prop="userId" label="会员编号"></el-table-column>
					<el-table-column label="分类">
						<template slot-scope="scope">
							<span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.classify == 2">微信公众号</span>
							<span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.classify == 3">微信小程序</span>
						</template>
					</el-table-column>
					<el-table-column label="状态">
						<template slot-scope="scope">
							<span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.state == 0">待支付</span>
							<span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.state == 1">支付成功</span>
							<span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.state == 2">支付失败</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="170">
					</el-table-column>
					<el-table-column prop="payTime" label="支付时间" width="170">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange4" @current-change="handleCurrentChange4"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="rechargeData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="积分明细" name="eighthjf">
				<el-table v-loading="tableDataLoading" :data="jifenData.records">
					<el-table-column prop="id" label="编号" width="100"></el-table-column>
					<!-- <el-table-column prop="title" label="标题" width="250"></el-table-column> -->
					<el-table-column prop="content" label="内容"></el-table-column>
					<el-table-column prop="money" label="金额" width="100">
						<template slot-scope="scope">
							<span style="color: #009900;" v-if="scope.row.type==1">+ {{ scope.row.num }}</span>
							<span style="color: #f56c6c;" v-else>- {{ scope.row.num }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeJf" @current-change="handleCurrentChangeJf"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="jifenData.total">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="我的订单" name="ninth">
				<el-table v-loading="tableDataLoading" :data="sendData.list">
					<el-table-column fixed prop = "ordersId" label = "编号" width = "80"></el-table-column>
					<el-table-column prop = "ordersNo" label = "订单编号" width = "180"></el-table-column>
					<el-table-column prop = "title" label = "购买资源/会员等级"  width = "180" align="center">
						<template slot-scope = "scope">
							<span v-if = "scope.row.ordersType == 1">{{scope.row.title}}</span>
							<span v-if = "scope.row.ordersType == 2 && scope.row.vipNameType==0">月卡</span>
							<span v-if = "scope.row.ordersType == 2 && scope.row.vipNameType==1">季卡</span>
							<span v-if = "scope.row.ordersType == 2 && scope.row.vipNameType==2">年卡</span>
						</template>
					</el-table-column>
					<el-table-column prop = "payWay" label = "订单类型"  width = "120">
						<template slot-scope = "scope">
							<span v-if = "scope.row.ordersType == 1">资源</span>
							<span v-if = "scope.row.ordersType == 2">会员</span>
						</template>
					</el-table-column>
					<el-table-column prop = "payWay" label = "支付方式"  width = "120">
						<template slot-scope = "scope">
							<span v-if = "scope.row.payWay == null">暂无</span>
							<span v-if = "scope.row.payWay == 1">微信小程序</span>
							<span v-if = "scope.row.payWay == 2">微信公众号</span>
							<span v-if = "scope.row.payWay == 3">微信APP</span>
							<span v-if = "scope.row.payWay == 4">支付宝</span>
							<span v-if = "scope.row.payWay == 5">会员免费</span>
						</template>
					</el-table-column>
					<el-table-column prop = "payMoney" label = "金额"  width = "120"></el-table-column>
					<el-table-column prop = "status" label = "状态" width="80">
						<template slot-scope = "scope">
							<span v-if = "scope.row.status == null">待支付</span>
							<span v-if = "scope.row.status == 0">待支付</span>
							<span v-if = "scope.row.status == 1">已支付</span>
							<span v-if = "scope.row.status == 2">已退款</span>
						</template>
					</el-table-column>
					<el-table-column prop = "refundContent" label = "退款原因" ></el-table-column>
					<el-table-column prop = "createTime" label = "创建时间" width = "200" ></el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange5" @current-change="handleCurrentChange5"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="sendData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<!-- <el-tab-pane label="我的资源" name="tenth">
				<el-table v-loading="tableDataLoading" :data="takeData.list">
					<el-table-column prop="courseId" label="编号"></el-table-column>
					<el-table-column prop="title" label="资源名称" width="150">
					</el-table-column>
					<el-table-column prop="courseClassification" label="资源分类" width="120">
						<template slot-scope="scope">
							<span v-if="scope.row.courseClassification">{{scope.row.courseClassification.classificationName}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="content" label="图片" width="120">
						<template slot-scope="scope">
							<img v-if="scope.row.titleImg" :src="scope.row.titleImg" width="100" height="100" />
						</template>
					</el-table-column>
					<el-table-column prop="price" label="价格" width="150">
					</el-table-column>
					<el-table-column prop="payNum" label="购买次数" width="150">
					</el-table-column>
					<el-table-column prop="courseLabel" label="资源标签">
					</el-table-column>
					<el-table-column prop="updateTime" label="更新时间">
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeZt" @current-change="handleCurrentChangeZy"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="takeData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane> -->
			<!-- <el-tab-pane label="我的评论" name="myComment">
				<el-table v-loading="tableDataLoading" :data="pinglunData.list">
					<el-table-column prop="courseCommentId" label="编号" width="80"></el-table-column>
					<el-table-column prop="title" label="评论资源" width="150">
					</el-table-column>
					<el-table-column prop="titleImg" label="资源图片" width="150">
						<template slot-scope="scope">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px" :src="scope.row.titleImg" alt="" slot="reference">
								<img style="width: 200px; height: 200px" :src="scope.row.titleImg" alt="">
							</el-popover>
						</template>
					</el-table-column>
					<el-table-column prop="content" label="评论内容"></el-table-column>
					<el-table-column prop="goodsNum" label="点赞次数" width="100"></el-table-column>
					<el-table-column prop="create_time" label="创建时间" width="160"></el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange6" @current-change="handleCurrentChange6"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="pinglunData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane> -->
			<el-tab-pane label="我的追剧" name="autonym">
				<el-table v-loading="tableDataLoading" :data="shoucangData.records">
					<el-table-column prop="courseId" label="编号"></el-table-column>
					<el-table-column prop="title" label="资源名称" width="150">
					</el-table-column>
					<el-table-column prop="courseClassification" label="资源分类" width="120">
						<template slot-scope="scope">
							<span v-if="scope.row.courseClassification">{{scope.row.courseClassification.classificationName}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="content" label="图片" width="120">
						<template slot-scope="scope">
							<img v-if="scope.row.titleImg" :src="scope.row.titleImg" width="100" height="100" />
						</template>
					</el-table-column>
					<el-table-column prop="price" label="价格" width="150">
					</el-table-column>
					<el-table-column prop="payNum" label="购买次数" width="150">
					</el-table-column>
					<el-table-column prop="courseLabel" label="资源标签">
					</el-table-column>
					<el-table-column prop="updateTime" label="更新时间">
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeSc" @current-change="handleCurrentChangeSc"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="shoucangData.total">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="我的喜欢" name="dianzan">
				<el-table v-loading="tableDataLoading" :data="shoucangData.records">
					<el-table-column prop="courseId" label="编号"></el-table-column>
					<el-table-column prop="title" label="资源名称" width="150">
					</el-table-column>
					<el-table-column prop="courseClassification" label="资源分类" width="120">
						<template slot-scope="scope">
							<span v-if="scope.row.courseClassification">{{scope.row.courseClassification.classificationName}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="content" label="图片" width="120">
						<template slot-scope="scope">
							<img v-if="scope.row.titleImg" :src="scope.row.titleImg" width="100" height="100" />
						</template>
					</el-table-column>

					<el-table-column prop="price" label="价格" width="150">
					</el-table-column>
					<el-table-column prop="payNum" label="购买次数" width="150">
					</el-table-column>
					<el-table-column prop="courseLabel" label="资源标签">
					</el-table-column>
					<el-table-column prop="updateTime" label="更新时间">
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeSc" @current-change="handleCurrentChangeSc"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="shoucangData.total">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="观看记录" name="liulanjilu">
				<el-table v-loading="tableDataLoading" :data="shoucangData.records">
					<el-table-column prop="courseId" label="编号"></el-table-column>
					<el-table-column prop="title" label="资源名称" width="150">
					</el-table-column>
					<el-table-column prop="courseClassification" label="资源分类" width="120">
						<template slot-scope="scope">
							<span v-if="scope.row.courseClassification">{{scope.row.courseClassification.classificationName}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="content" label="图片" width="120">
						<template slot-scope="scope">
							<img v-if="scope.row.titleImg" :src="scope.row.titleImg" width="100" height="100" />
						</template>
					</el-table-column>
					<el-table-column prop="courseDetailsName" label="剧集名称" width="150">
					</el-table-column>
					<el-table-column prop="price" label="价格" width="150">
					</el-table-column>
					<el-table-column prop="payNum" label="购买次数" width="150">
					</el-table-column>
					<el-table-column prop="courseLabel" label="资源标签">
					</el-table-column>
					<el-table-column prop="isOver" label="状态" align="center">
						<template slot-scope="scope">
							<span v-if="scope.row.isOver==1">已完结</span>
							<span v-else>更新{{scope.row.courseDetailsCount}}集</span>
						</template>
					</el-table-column>
					<el-table-column prop="updateTime" label="更新时间">
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeSc" @current-change="handleCurrentChangeSc"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="shoucangData.total">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="我的消息" name="xiaoxi">
				<el-table v-loading="tableDataLoading" :data="userData.list">
					<el-table-column prop="id" label="编号" width="80"></el-table-column>
					<el-table-column prop="title" label="消息名称" width="250">
					</el-table-column>
					<el-table-column prop="content" label="内容">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间" width="180">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange7" @current-change="handleCurrentChange7"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="userData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="我的团队" name="yaoqing">
				<el-table v-loading="tableDataLoading" :data="yaoqingData.list">
					<el-table-column prop="courseId" label="编号"width="120">
						<template slot-scope="scope">
							<div>{{scope.$index+1}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="userName" label="用户昵称" width="250">
						<template slot-scope="scope">
							<span style="color: #4f9dec;" @click="updates(scope.row)">{{scope.row.userName}}</span>
						</template>

					</el-table-column>
					<el-table-column prop="avatar" label="用户头像">
						<template slot-scope="scope">
							<img v-if="scope.row.avatar" :src="scope.row.avatar" width="60" height="60" />
						</template>
					</el-table-column>
					<el-table-column prop="money" label="奖励金额">
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange8" @current-change="handleCurrentChange8"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="yaoqingData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<!-- 不可提现添加金额 -->
			<el-dialog title="修改金额" :visible.sync="dialogFormVisible1" center>
				<div style="margin-bottom: 10px;">
				  <span style="width: 200px;display: inline-block;text-align: right;">修改模式：</span>
				  <el-radio-group v-model="types">
				    <el-radio :label="1">增加</el-radio>
				    <el-radio :label="2">减少</el-radio>
				  </el-radio-group>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">金额：</span>
					<el-input style="width:50%;" v-model="money" type="number" :min="0" :max="100" :controls="false"
						placeholder="请输入金额"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible1 = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeTo1()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改积分 -->
			<el-dialog title="修改积分" :visible.sync="dialogFormVisibleJf" center>
				<div style="margin-bottom: 10px;">
				  <span style="width: 200px;display: inline-block;text-align: right;">修改模式：</span>
				  <el-radio-group v-model="types">
				    <el-radio :label="1">增加</el-radio>
				    <el-radio :label="2">减少</el-radio>
				  </el-radio-group>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">积分：</span>
					<el-input style="width:50%;" v-model="money" type="number" :min="0" :max="100" :controls="false"
						placeholder="请输入积分"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleJf = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeToJ()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改比例 -->
			<el-dialog :title="titleBl" :visible.sync="dialogFormVisible2" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">比例：</span>
					<el-input style="width:50%;" v-model="proportion" type="number" :min="0" :controls="false"
						:placeholder="titleBl"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible2 = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeTo2()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改信息 -->
			<el-dialog title="修改信息" :visible.sync="dialogFormVisibleU" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">微信名称：</span>
					<el-input style="width:50%;" v-model="userNameU" type="text"  placeholder="请输入微信名称"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">手机号：</span>
					<el-input style="width:50%;" v-model="phoneU" type="text"  placeholder="请输入手机号"></el-input>
				</div>
				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">邀请人邀请码：</span>
					<el-input style="width:50%;" v-model="inviterCodeU" type="text"  placeholder="请输入邀请人邀请码"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">邀请码：</span>
					<el-input style="width:50%;" v-model="invitationCodeU" type="text"  placeholder="请输入邀请码"></el-input>
				</div> -->
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">图像：</span>
					<div style="display: inline-block;width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="avatarU"
							:action="$http.adornUrl('alioss/upload')"  :show-file-list="false"
							:on-success="handleAvatarSuccess1">
							<img v-if="avatarU" :src="avatarU" class="avatar"
								style="width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
						</el-upload>
					</div>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleU = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeToU()">确 定</el-button>
				</div>
			</el-dialog>
		</el-tabs>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				openValue: 1,
				closeValue: 0,
				state: 0,
				limit: 10,
				page: 1,
				userId: '',
				money: '',
				title: '',
				content: '',
				phone: '',
				startTime: '',
				endTime: '',
				invitationCode: '',
				feiDatamin: '',
				info: {
					stockDate: this.getNowTime(), //日期
				},
				info2: {
					stockDate2: this.getNowTime2(), //日期
				},
				tablemoney: {},
				tablenum: {},
				tableData: {},
				userData: [],
				tableDatamin: [],
				withdrawData: [],
				walletData: [],
				jifenData:{},
				rechargeData: [],
				sendData: [],
				takeData: [],
				shoucangData:{},
				yaoqingData:{},
				pinglunData:{},
				activeName: 'first',
				dialogFormVisible1: false,
				dialogFormVisibleJf:false,
				tableDataLoading: true,
				search: '',
				classifyIds: 0,
				statusId: -1,
				myPhone: '',
				types:0,
				dialogFormVisible2: false,
				titleBl: '修改佣金比例',
				proportion:'',
				// 修改信息
				dialogFormVisibleU:false,
				inviterCodeU:'',
				avatarU:'',
				userNameU:'',
				phoneU:'',
				invitationCodeU:'',
				classify:1,
				qianbao:{},
				jifen:{},
			}
		},
		methods: {
			// 返回上一级
			prev() {
				this.$router.back()
			},
			// 详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() //得到月份
				var date = now.getDate() //得到日期
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			//处理默认选中当前日期
			getNowTime2() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() - now.getMonth() //得到月份
				var date = now.getDate() - now.getDate() + 1 //得到日期
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			// tabs切换
			handleClick(tab, event) {
				if (tab._props.label == '用户详情') {
					this.dataSelect()
				}
				if (tab._props.label == '我的追剧') {
					this.limit = 10
					this.page = 1
					this.classify = 1
					this.teamSelect()
				}
				if (tab._props.label == '我的喜欢') {
					this.limit = 10
					this.page = 1
					this.classify = 2
					this.teamSelect()
				}
				if (tab._props.label == '观看记录') {
					this.limit = 10
					this.page = 1
					this.classify = 3
					this.teamSelect()
				}
				if (tab._props.label == '我的评论') {
					this.limit = 10
					this.page = 1
					this.takeSelect()
				}
				if (tab._props.label == '发布信息') {
					this.limit = 10
					this.page = 1
					this.InformationSelect()
				}
				if (tab._props.label == '我的消息') {
					this.state = 5
					this.userSelect()
				}
				if (tab._props.label == '提现记录') {
					this.limit = 10,
						this.page = 1,
						this.withdrawSelect()
				}
				if (tab._props.label == '钱包明细') {
					this.limit = 10
					this.page = 1
					this.walletSelect()
				}
				if (tab._props.label == '充值明细') {
					this.limit = 10
					this.page = 1
					this.rechargeSelect()
				}
				if (tab._props.label == '积分明细') {
					this.limit = 10
					this.page = 1
					this.jifenSelect()
				}

				if (tab._props.label == '我的订单') {
					this.limit = 10
					this.page = 1
					this.state = 0
					this.sendSelect()
				}
				if (tab._props.label == '我的资源') {
					this.limit = 10
					this.page = 1
					this.state = 0
					this.kechengSelect()
				}
				if (tab._props.label == '我的团队') {
					this.limit = 10
					this.page = 1
					this.state = 0
					this.yaoqingSelect()
				}

				if (tab._props.label == '消息推送') {
					this.limit = 10
					this.page = 1
					this.state = 8
					this.flag = 1
					this.userSelect()
				}
			},
			handleSizeChangeSc(val) {
				this.limit = val
				this.teamSelect()
			},
			handleCurrentChangeSc(val) {
				this.page = val
				this.teamSelect()
			},
			handleSizeChange1(val) {
				this.limit = val
				this.InformationSelect()
			},
			handleCurrentChange1(val) {
				this.page = val
				this.InformationSelect()
			},
			handleSizeChange2(val) {
				this.limit = val
				this.withdrawSelect()
			},
			handleCurrentChange2(val) {
				this.page = val
				this.withdrawSelect()
			},
			handleSizeChange3(val) {
				this.limit = val
				this.walletSelect()
			},
			handleCurrentChange3(val) {
				this.page = val
				this.walletSelect()
			},
			handleSizeChangeJf(val) {
				this.limit = val
				this.jifenSelect()
			},
			handleCurrentChangeJf(val) {
				this.page = val
				this.jifenSelect()
			},
			handleSizeChange4(val) {
				this.limit = val
				this.rechargeSelect()
			},
			handleCurrentChange4(val) {
				this.page = val
				this.rechargeSelect()
			},
			handleSizeChange5(val) {
				this.limit = val
				this.sendSelect()
			},
			handleCurrentChange5(val) {
				this.page = val
				this.sendSelect()
			},
			handleSizeChange6(val) {
				this.limit = val
				this.takeSelect()
			},
			handleCurrentChange6(val) {
				this.page = val
				this.takeSelect()
			},
			handleSizeChange7(val) {
				this.limit = val
				this.userSelect()
			},
			handleCurrentChange7(val) {
				this.page = val
				this.userSelect()
			},
			handleSizeChange8(val) {
				this.limit = val
				this.yaoqingSelect()
			},
			handleCurrentChange8(val) {
				this.page = val
				this.yaoqingSelect()
			},
			handleSizeChangeZy(val) {
				this.limit = val
				this.kechengSelect()
			},
			handleCurrentChangeZy(val) {
				this.page = val
				this.kechengSelect()
			},
			timeDate2() {
				this.rechargeSelect()
			},
			// 可提现金额
			withdraw(id, val) {
				if (val == 0) {
					this.$message({
						message: '可提现金额为0元',
						type: 'error',
						duration: 1500,
						onClose: () => {
							this.dataSelect()
						}
					})
				} else {
					this.$confirm(`确定要推送提现消息吗?`, '提示', {
						confirmButtonText: '确定',
						cancelButtonText: '取消',
						type: 'warning'
					}).then(() => {
						this.$http({
							url: this.$http.adornUrl(`user/notification/${id}`),
							method: 'post',
							data: this.$http.adornData({})
						}).then(({
							data
						}) => {
							this.$message({
								message: '推送成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						})
					}).catch(() => {})
				}
			},
			// 不可提现金额充值
			rechargenone() {
				this.types = 0
				this.money = ''
				this.dialogFormVisible1 = true
			},
			StairNoticeTo1() {
				let userId = this.$route.query.userId
				if (this.types == 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择修改模式',
						type: 'warning'
					})
					return
				}
				if (this.money == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入金额',
						type: 'warning'
					})
					return
				}
				if(this.types==1){
					var urls = `user/addCannotMoney/${userId}/${this.money}`
				}else{
					var urls = `user/subCannotMoney/${userId}/${this.money}`
				}
				this.$http({
					url: this.$http.adornUrl(urls),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '修改金额成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.money = ''
								this.dataSelect()
							}
						})
						this.dialogFormVisible1 = false
					} else {
						this.$message.error(data.msg)
					}
				})
			},
			// 修改积分弹框
			rechargenoneJf() {
				this.types = 0
				this.money = ''
				this.dialogFormVisibleJf = true
			},
			// 修改积分
			StairNoticeToJ() {
				let userId = this.$route.query.userId
				if (this.types == 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择修改模式',
						type: 'warning'
					})
					return
				}
				if (this.money == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入积分',
						type: 'warning'
					})
					return
				}
				// if(this.types==1){
				// 	var urls = `user/addCannotMoney/${userId}/${this.money}`
				// }else{
				// 	var urls = `user/subCannotMoney/${userId}/${this.money}`
				// }
				this.$http({
					url: this.$http.adornUrl('integral/updateUserIntegral'),
					method: 'post',
					params: this.$http.adornParams({
						'integral':this.money,
						'type':this.types,
						'userId':userId
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.money = ''
								this.dataSelect()
							}
						})
						this.dialogFormVisibleJf = false
					} else {
						this.$message.error(data.msg)
					}
				})
			},
			// 更改状态
			stateChange(userId) {
				this.$confirm(`确定要更改用户状态吗?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`user/updateUserStateById?userId=${userId}`),
						method: 'post',
						data: this.$http.adornData({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 获取数据列表
			dataSelect() {
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl(`user/${userId}`),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					if (data.code === 0) {
						console.log('data', data)
						let returnData = data.data
						this.tablenum = returnData
						this.tablemoney = returnData
						this.tableData = returnData.userEntity
						this.phone = returnData.userEntity.phone
						this.invitationCode = returnData.userEntity.invitationCode
					}
				})
				this.$http({
					url: this.$http.adornUrl('moneyDetails/selectUserMoney'),
					method: 'get',
					params: this.$http.adornParams({
						'userId':userId
					})
				}).then(({
					data
				}) => {
					if (data.code === 0) {
						console.log('data', data)
						let returnData = data.data
						this.qianbao = returnData
					}
				})
				this.$http({
					url: this.$http.adornUrl('integral/selectByUserId'),
					method: 'get',
					params: this.$http.adornParams({
						'userId':userId
					})
				}).then(({
					data
				}) => {
					if (data.code === 0) {
						console.log('data', data)
						let returnData = data.data
						this.jifen = returnData
					}
				})
			},
			// 我的追剧
			teamSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('courseCollect/selectByUserId'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
						'classify':this.classify
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.shoucangData = returnData
				})
			},
			// 获取用户/任务消息
			userSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('message/selectMessageByUserId'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
						'state': this.state
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.userData = returnData
				})
			},
			// 获取用户提现记录
			withdrawSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('cash/selectPayDetails'),
					method: 'get',
					params: this.$http.adornParams({
						'page':this.page,
						'limit':this.limit,
						// 'zhifubaoName':'',
						// 'zhifubao':this.zhifubao,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.withdrawData = returnData
				})
			},
			// 获取钱包明细
			walletSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('moneyDetails/queryUserMoneyDetails'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.walletData = returnData
				})
			},
			// 积分明细
			jifenSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('integral/details'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.jifenData = returnData
				})
			},
			// 获取充值明细
			rechargeSelect() {
				if (this.endTime == '') {
					this.endTime = this.info.stockDate
				}
				if (this.startTime == '') {
					this.startTime = this.info2.stockDate2
				}
				let userId = this.$route.query.userId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('cash/selectUserRechargeByUserId'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'endTime': this.endTime,
						'startTime': this.startTime,
						'userId': userId,
						'state':''
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.tableDataLoading = false
						let returnData = data.data
						this.rechargeData = returnData
					}
					if (data.code == 500) {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 2500,
							onClose: () => {
								this.tableDataLoading = false
							}
						})
					}
				})
			},
			// 我的资源
			kechengSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('course/selectCourseUserbyid'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						// 'status': this.state,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.takeData = returnData
				})
			},
			// 我的订单
			sendSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('order/selectOrders'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						// 'ordersNo': '',
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.sendData = returnData
				})
			},
			// 我的评论
			takeSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('courseComment/selectCourseCommentUser'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.pinglunData = returnData
				})
			},
			// 发布信息数据
			InformationSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl(`information/selectInformationList`),
					method: 'get',
					params: this.$http.adornParams({
						'userId': userId,
						'page': this.page,
						'limit': this.limit,
						'search': this.search,
						'classify': this.classifyIds,
						'status': this.statusId,
						'phone': this.myPhone
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					for (var i in data.data.records) {
						if (data.data.records[i].img) {
							data.data.records[i].imgs = data.data.records[i].img.split(',')
						}

					}
					let returnData = data.data
					this.userData = returnData
				})
			},
			// 查询
			phoneSelect() {
				this.page = 1
				this.InformationSelect()
			},
			// 重置
			cleans2() {
				this.status = -1
				this.classify = 0
				this.phone = ''
				this.search = ''
				this.page = 1
				this.InformationSelect()
			},
			// 我的团队列表
			yaoqingSelect(){
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('invite/selectInviteByUserIdLists'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data.pageUtils
					this.yaoqingData = returnData
				})
			},
			xiugai(row, text) {

				if (text == 'rate') {
					this.titleBl = '修改推广收益比例'
					this.proportion = row.rate
				}

				this.texts = text
				this.userIdss = row.userId
				this.dialogFormVisible2 = true
			},
			StairNoticeTo2() {
				var datas = {}
				if (this.texts == 'rate') {
					datas.rate = this.proportion
				}


				datas.userId = this.userIdss
				this.$http({
					url: this.$http.adornUrl('user/updateUserByUserId'),
					method: 'post',
					data: this.$http.adornData(datas)
				}).then(({
					data
				}) => {
					console.log('data', data)
					if(data.code==0){
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dialogFormVisible2 = false
								this.dataSelect()
							}
						})
					}else{
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {
							}
						})
					}

				})
			},
			// 设置会员
			updateVip(row) {

				this.$http({
					url: this.$http.adornUrl('vipDetails/sendVip'),
					method: 'post',
					params: this.$http.adornParams({
						'userId': row.userId,
						'num': 30
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					}

				})
			},
			// 取消会员
			quxiaoVip(row) {
				this.$confirm(`确定要取消会员?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('vipDetails/deleteVip'),
						method: 'post',
						// data: this.$http.adornData({
						params: this.$http.adornParams({
							'userId': row.userId
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'warning',
								duration: 1500,
								onClose: () => {}
							})
						}

					})
				})
			},
			ageChangeU(row) {
				this.inviterCodeU = row.inviterCode
				this.avatarU = row.avatar
				this.userNameU = row.userName
				this.phoneU = row.phone
				this.invitationCodeU = row.invitationCode

				this.dialogFormVisibleU = true
			},
			handleAvatarSuccess1(file, fileList) {
				this.avatarU = file.data
			},
			StairNoticeToU() {
				if (this.userNameU === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '微信名称不能为空',
						type: 'warning'
					})
					return
				}
				if (this.phoneU === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '手机号不能为空',
						type: 'warning'
					})
					return
				}
				if (this.avatarU === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '头像不能为空',
						type: 'warning'
					})
					return
				}
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('user/updateUserByUserId'),
					method: 'post',
					data: this.$http.adornData({
						'userId': userId,
						// 'inviterCode': this.inviterCodeU,
						'avatar': this.avatarU,
						'userName': this.userNameU,
						'phone': this.phoneU,
						// 'invitationCode': this.invitationCodeU,
					})
				}).then(({
					data
				}) => {
					console.log('data', data)
					if(data.code==0){
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dialogFormVisibleU = false
								this.dataSelect()
							}
						})
					}else{
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {
							}
						})
					}

				})
			},
		},
		mounted() {
			this.dataSelect()
		}
	}
</script>

<style scoped="scoped">
	.detailtabel h2 {
		margin-top: 0;
	}

	.detailtabel .table_main table {
		width: 100%;
	}

	.detailtabel .table_main {
		border: 1px solid #e8e8e8;
	}

	.detailtabel table tr {
		border-bottom: 1px solid #e8e8e8;
	}

	.detailtabel table tr th {
		background-color: #fafafa;
		padding: 16px 24px;
		border-right: 1px solid #e8e8e8;
		border-bottom: 1px solid #e8e8e8;
	}

	.detailtabel table tr td {
		padding: 16px 24px;
		border-right: 1px solid #e8e8e8;
		border-bottom: 1px solid #e8e8e8;
	}

	.detailtabel table th {
		color: rgba(0, 0, 0, .85);
		font-weight: 400;
		font-size: 14px;
		line-height: 1.5;
	}

	.border-rt {
		border-right: none !important;
	}

	.border-bt {
		border-bottom: none !important;
	}
</style>
