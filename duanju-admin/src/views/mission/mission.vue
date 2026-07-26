<template>
	<el-tabs v-model="activeName" @tab-click="handleClick">
		<el-tab-pane label="视频信息" name="first">
			<!-- <div style="display: inline-block;">
				<span>类别：</span>
				<el-select v-model="fenleiIdT" style="width:150px;margin-left: 10px;" @change="animeDat(fenleiIdT)">
					<el-option v-for="item in statesnum" :key="item.classificationId" :label="item.classificationName"
						:value="item.classificationId">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
			</div> -->
			<!-- <div style="position: relative;display: inline-block;margin: 5px;">
				<span>是否推荐：</span>
				<el-select v-model="isRecommends" style="width:150px;margin-left: 10px;"
					@change="animeDat2(isRecommends)">
					<el-option v-for="item in isRecommendNum" :key="item.classificationId"
						:label="item.classificationName" :value="item.classificationId">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;&nbsp;

			</div> -->
			<div style="position: relative;display: inline-block;margin: 5px;">
				<span>上下架：</span>
				<el-select v-model="statuss" style="width:150px;margin-left: 10px;" @change="animeDat3(statuss)">
					<el-option v-for="item in statusNum" :key="item.classificationId" :label="item.classificationName"
						:value="item.classificationId">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
			</div>
			<div style="position: relative;display: inline-block;margin: 5px;">
				<span>标题：</span>
				<el-input style="width: 200px;" @keydown.enter.native="select" placeholder="请输入标题" v-model="contentT">
				</el-input>&nbsp;&nbsp;
			</div>

			<div style="position: relative;display: inline-block;margin: 5px;">
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="select">查询
				</el-button>
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleans">重置
				</el-button>
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="tianjia"
					:disabled="!isAuth('mission:add')">添加
				</el-button>
				<el-button style='margin:0 0 20px 20px;' size="mini" type="primary" icon="document"
					@click="shangxiajiaClcik()" :disabled="checkBoxData.length <= 0 || !isAuth('mission:update')">
					批量上下架
				</el-button>
				<el-button style='margin:0 0 20px 20px;' size="mini" type="warning" icon="document"
					@click="deleteClcik()" :disabled="checkBoxData.length <= 0 || !isAuth('mission:update')">
					批量删除
				</el-button>
				<el-button style="margin-left:15px;" size="mini" plain type="primary" icon="document" @click="tongbu"
					v-if="show">同步资源
				</el-button>
				<el-button style="margin-left:15px;" size="mini" type="success" icon="document"
					:loading="wxSyncLoading" @click="syncWechatDrama" :disabled="!isAuth('mission:add')">
					一键同步微信短剧
				</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="tableData.list" @selection-change="changeFun">
				<el-table-column type="selection">
				</el-table-column>
				<el-table-column prop="courseId" label="编号" fixed="left"></el-table-column>

				<!-- <el-table-column prop="classificationName" label="类别" width="100">
				</el-table-column> -->
				<!-- <el-table-column prop="bannerName" label="分类" width="100">
				</el-table-column> -->
				<el-table-column prop="title" label="标题" width="180" fixed="left"></el-table-column>
				<el-table-column prop="details" label="资源介绍" width="200">
					<template slot-scope="scope">
						<div
							style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden; height: 70px;">
							{{scope.row.details}}
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="titleImg" label="封面图" width="150">
					<template slot-scope="scope">
						<el-popover placement="top-start" title="" trigger="hover">
							<img style="width: 50px; height: 50px" :src="scope.row.titleImg" alt="" slot="reference">
							<img style="width: 200px; height: 200px" :src="scope.row.titleImg" alt="">
						</el-popover>
					</template>
				</el-table-column>
				<!-- <el-table-column prop="bannerImg" label="轮播图" width="150">
					<template slot-scope="scope">
						<div v-if="scope.row.bannerImg == null || scope.row.bannerImg == ''">
							暂无图片
						</div>
						<div v-else-if="scope.row.bannerImg" style="display:flex;flex-wrap: wrap;">
							<div v-for="item in scope.row.bannerImg.split(',')" style="margin: 2px;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
									<img style="width: 200px; height: 200px" :src="item" alt="">
								</el-popover>
							</div>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="img" label="详情图" width="150">
					<template slot-scope="scope">
						<div v-if="scope.row.img == null || scope.row.img == ''">
							暂无图片
						</div>
						<div v-else-if="scope.row.img" style="display:flex;flex-wrap: wrap;">
							<div v-for="item in scope.row.img.split(',')" style="margin: 2px;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
									<img style="width: 200px; height: 200px" :src="item" alt="">
								</el-popover>
							</div>
						</div>
						<div v-else>
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px" :src="scope.row.studentImg" alt=""
									slot="reference">
								<img style="width: 200px; height: 200px" :src="scope.row.studentImg" alt="">
							</el-popover>
						</div>
					</template>
				</el-table-column> -->
				<el-table-column prop="isPrice" label="是否收费" width="100">
					<template slot-scope="scope">
						<span v-if="scope.row.isPrice == 1 ">是</span>
						<span v-else>免费</span>

					</template>
				</el-table-column>
				<!-- <el-table-column prop="isRecommend" label="是否推荐" width="160">
					<template slot-scope="scope">
						<el-switch v-model="scope.row.isRecommend" @change="change(scope.row)" :active-value="openValue"
							:inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
						</el-switch>
					</template>
				</el-table-column> -->
				<el-table-column prop="courseLabel" label="资源标签" width="160"></el-table-column>
				<el-table-column prop="price" label="剧价格"></el-table-column>
				<el-table-column prop="jifen" label="剧积分价格"></el-table-column>
				<el-table-column prop="payNum" label="购买次数" width="60"></el-table-column>

				<el-table-column prop="viewCounts" label="播放量" width="60"></el-table-column>
				<el-table-column prop="isRecommend" label="上下架" width="100">
					<template slot-scope="scope">
						<el-switch v-model="scope.row.status" @change="change1(scope.row.status,scope.row.courseId)"
							:active-value="openValue1" :inactive-value="closeValue1" active-color="#13ce66"
							inactive-color="#ff4949">
						</el-switch>
					</template>
				</el-table-column>
				<el-table-column prop="isOver" label="是否完结" align="center">
					<template slot-scope="scope">
						<el-switch v-model="scope.row.isOver" @change="changeW(scope.row)" :active-value="openValue"
							:inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
						</el-switch>
					</template>
				</el-table-column>
				<el-table-column fixed="right" prop="isDelete" label="状态" width="100">
					<template slot-scope="scope">
						<span style="color: #4f9dec;" v-if="scope.row.isDelete === 0 ">使用中</span>
						<span v-if="scope.row.isDelete === 1 ">已删除</span>
					</template>
				</el-table-column>
				<el-table-column prop="updateTime" label="更新时间" width="160"></el-table-column>
				<el-table-column fixed="right" label="操作" width="200" align="center">
					<template slot-scope="scope">
						<el-button class="btns" size="mini" type="primary" @click="refund1(scope.row)">视频资源
						</el-button>

						<el-button class="btns" size="mini" type="primary" :disabled="!isAuth('mission:update')"
							@click="xiugai(scope.row)">修改</el-button>
						<el-button class="btns" size="mini" type="warning" @click="shouru(scope.row)">收入统计
						</el-button>
						<el-button class="btns" size="mini" type="danger" :disabled="!isAuth('mission:delete')"
							@click="deles(scope.row)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 40, 100, 200, 500]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
			<!-- 添加/修改资源列表 -->
			<el-dialog :title="titles" :visible.sync="dialogFormVisible1" center width="80%">
				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">类别：</span>
					<el-select v-model="fenleiId1" style="width:50%;">
						<el-option v-for="item in statesnum1" :key="item.classificationId"
							:label="item.classificationName" :value="item.classificationId">
						</el-option>
					</el-select>
				</div> -->
				<!-- <div style="margin-bottom: 10px;clear: both;">
					<span style="width: 200px;display: inline-block;text-align: right;">分类：</span>
					<el-select v-model="fenleiId2" style="width:50%;" @change="animeDat1(fenleiId2)">
						<el-option v-for="item in statesnum2" :key="item.id" :label="item.name" :value="item.id">
						</el-option>
					</el-select>
				</div> -->
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">标题：</span>
					<el-input v-model="title" style="width:50%;" placeholder="请输入标题"></el-input>
				</div>
				<div style="margin-bottom: 10px;display:flex;">
					<span style="width: 200px;display: inline-block;text-align: right;">封面图：</span>
					<div
						style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="titleImg"
							:action="$http.adornUrl('alioss/upload')"  :show-file-list="false"
							:on-success="handleAvatarSuccess5">
							<img v-if="titleImg" :src="titleImg" class="avatar"
								style="border-radius: 6px;width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon iconss"></i>
						</el-upload>
					</div>
				</div>
				<!-- <div style="margin-bottom: 10px;display:flex;">
					<span style="width: 200px;display: inline-block;text-align: right;">轮播图：</span>
					<div class="imgs" v-for="(item,index) in bannerImg" :key="index">
						<img width="100%" class="images" height="100%" :src="item" alt="">
						<span class="dels">
							<i class="el-icon-delete" @click="delsB(index)"></i>
						</span>
					</div>
					<div class="imgs">
						<el-upload :action="$http.adornUrl('alioss/upload')"
							list-type="picture-card" :show-file-list="false" :on-success="handleRemoveB">
							<i class="el-icon-plus"></i>
						</el-upload>
					</div>
				</div>
				<div style="margin-bottom: 10px;display:flex;">
					<span style="width: 200px;display: inline-block;text-align: right;">详情图：</span>
					<div class="imgs" v-for="(item,index) in img" :key="index">
						<img width="100%" class="images" height="100%" :src="item" alt="">
						<span class="dels">
							<i class="el-icon-delete" @click="dels(index)"></i>
						</span>
					</div>
					<div class="imgs">
						<el-upload :action="$http.adornUrl('alioss/upload')"
							list-type="picture-card" :show-file-list="false" :on-success="handleRemove">
							<i class="el-icon-plus"></i>
						</el-upload>
					</div>
				</div> -->
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">购买剧价格：</span>
					<el-input v-model="price" type="number" style="width:50%;" placeholder="请输入购买剧价格"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">剧积分价格：</span>
					<el-input v-model="jifen" type="number" style="width:50%;" placeholder="请输入购买剧积分价格"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">购买次数：</span>
					<el-input v-model="payNum" type="number" style="width:50%;" placeholder="请输入购买次数"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">播放量：</span>
					<el-input v-model="viewCounts" type="number" style="width:50%;" placeholder="请输入播放量"></el-input>
				</div>

				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">标签：</span>
					<span class="bqList" v-for="(item,index) in privilege" :key="index">{{item}}
						<span class="delss">
							<i class="el-icon-delete" @click="dels2(index)"></i>
						</span>
					</span>
					<el-input v-model="bq" style="width:200px;" placeholder="请输入标签" @keydown.enter.native="btnTj"></el-input>
					<el-button type="primary" class="tj" size="mini" icon="el-icon-edit" circle @click="btnTj">
					</el-button>
				</div>

				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">是否收费：</span>
					<el-radio-group v-model="isPrice">
						<el-radio :label="1">是</el-radio>
						<el-radio :label="2">免费</el-radio>
					</el-radio-group>
				</div>
				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">是否推荐：</span>
					<el-radio-group v-model="isRecommend">
						<el-radio :label="1">推荐</el-radio>
						<el-radio :label="0">普通</el-radio>
					</el-radio-group>
				</div> -->
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">上下架：</span>
					<el-radio-group v-model="statusType">
						<el-radio :label="1">上架</el-radio>
						<el-radio :label="2">下架</el-radio>
					</el-radio-group>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">视频介绍：</span>
					<el-input v-model="details" style="width:200px;display: none;" placeholder="请输入视频介绍"></el-input>
					<quill-editor ref="myTextEditor" v-model="details" :options="quillOption"
						style="padding-bottom: 50px;height: 300px;width: 72%;display: inline-table;margin-bottom: 60px;">
					</quill-editor>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible1 = false">取 消</el-button>
					<el-button type="primary" @click="addDetails()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 视频详情列表 -->
			<el-dialog title="视频详情列表" :visible.sync="dialogVisible" width="90%" center="">
				<div style="display: inline-block;">
					<div style="display: inline-block;">
						<span>是否推荐：</span>
						<el-select v-model="goods" style="width:150px;margin-left: 10px;" @change="chaxun()">
							<el-option v-for="item in goodsList" :key="item.classificationId" :label="item.classificationName"
								:value="item.classificationId">
							</el-option>
						</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="chaxun">
						查询</el-button>
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="addVideo">
						添加视频</el-button>
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="addVideoPl">
						批量导入视频</el-button>
					<el-button style='margin:0 0 20px 20px;' size="mini" type="primary" icon="document"
						@click="transferClcik()" :disabled="checkBoxData.length <= 0 || !isAuth('mission:update')">
						批量修改
					</el-button>
					<el-button style='margin:0 0 20px 20px;' size="mini" type="warning" icon="document"
						@click="deleteClcikJj()" :disabled="checkBoxData.length <= 0 || !isAuth('mission:update')">
						批量删除
					</el-button>
				</div>
				<el-table v-loading="tableDataLoadingJj" :data="curriList.list" @selection-change="changeFun">
					<el-table-column type="selection">
					</el-table-column>
					<el-table-column prop="courseDetailsId" label="编号" width="80" fixed="left"></el-table-column>
					<el-table-column prop="courseDetailsName" label="名称" fixed="left"></el-table-column>
					<el-table-column prop="titleImg" label="封面图">
						<template slot-scope="scope">
							<el-popover v-if="scope.row.titleImg" placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px" :src="scope.row.titleImg" alt=""
									slot="reference">
								<img style="width: 200px; height: 200px" :src="scope.row.titleImg" alt="">
							</el-popover>
							<span v-if="!scope.row.titleImg"> - </span>
						</template>
					</el-table-column>
					<el-table-column prop="content" label="介绍" width="200"></el-table-column>
					<el-table-column prop="goodNum" label="点赞数"></el-table-column>
					<el-table-column prop="price" label="集价格"></el-table-column>
					<el-table-column prop="jifen" label="集积分价格"></el-table-column>
					<el-table-column prop="isPrice" label="是否收费" align="center">
						<template slot-scope="scope">
							<div v-if="scope.row.isPrice == 1 ">是</div>
							<div v-else>免费</div>
							<el-switch v-model="scope.row.isPrice" @change="changeD(scope.row)"
								:active-value="openValue1" :inactive-value="closeValue1" active-color="#13ce66"
								inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column prop="videoUrl" label="视频" width="150">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('mission:update')"
								style="margin: 5px;" @click="playVideo(scope.row.videoUrl)">播放视频
							</el-button>
						</template>
					</el-table-column>
					<el-table-column prop="videoUrl" label="视频链接" width="200">
						<template slot-scope="scope">
							<span>
								<el-tag type="success" size="mini" @click="copyText(scope.row.videoUrl)" style="cursor: pointer;" >
									复制
								</el-tag>
								{{ scope.row.videoUrl }}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="good" label="是否推荐" align="center">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.good" @change="changeT(scope.row)" :active-value="openValue1"
								:inactive-value="closeValue1" active-color="#13ce66" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column label="操作" width="80" fixed="right">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('mission:update')"
								style="margin: 5px;" @click="compileVideo(scope.row)">编辑
							</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('mission:delete')"
								style="margin: 5px;" @click="deleteVideo(scope.row)">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
						:page-sizes="[10, 20, 30, 40, 100, 200, 500]" :page-size="limitS" :current-page="pageS"
						layout="total,sizes, prev, pager, next,jumper" :total="curriList.totalCount">
					</el-pagination>
				</div>
			</el-dialog>
			<!-- 添加视频 -->
			<el-dialog title="添加视频" :visible.sync="dialogFormVisible3" width="80%" center="">
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">标题：</span>
					<el-input v-model="title" style="width:50%;" placeholder="请输入资源标题"></el-input>
				</div>
				<div style="margin-bottom: 10px;display:flex;">
					<span style="width: 200px;display: inline-block;text-align: right;">封面图：</span>
					<div
						style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="titleImg"
							:action="$http.adornUrl('alioss/upload')"  :show-file-list="false"
							:on-success="handleAvatarSuccess5">
							<img v-if="titleImg" :src="titleImg" class="avatar"
								style="border-radius: 6px;width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon iconss"></i>
						</el-upload>
					</div>
				</div>
				<div style="margin-bottom: 10px;display:flex;">
					<span style="width: 200px;display: inline-block;text-align: right;">上传视频：</span>
					<div
						style=" width:200px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="url"
							:action="$http.adornUrl('alioss/upload')"  :show-file-list="false"
							multiple :on-success="handleAvatarSuccess3" :before-upload="beforeUploadVideo"
							:on-progress="beforeUpload">
							<video v-if="url" :src="url" class="avatar" controls="controls"
								style="width: 200px;height:148px;">您的浏览器不支持视频播放</video>
							<i v-else class="el-icon-plus avatar-uploader-icon iconss"></i>
							<el-progress type="circle" v-if="percentage>0 && percentage<=100" :percentage="percentage"
								color="#efefef"></el-progress>
						</el-upload>
					</div>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">视频链接：</span>
					<el-input v-model="url" style="width:50%;" placeholder="请输入视频链接"></el-input>
				</div>

				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">集价格：</span>
					<el-input v-model="price" type="number" style="width:50%;" placeholder="请输入集价格"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">集积分价格：</span>
					<el-input v-model="jifen" type="number" style="width:50%;" placeholder="请输入集积分价格"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">点赞数：</span>
					<el-input v-model="goodNum" type="number" style="width:50%;" placeholder="请输入点赞数"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">是否收费：</span>
					<el-radio-group v-model="isPrice">
						<el-radio :label="1">是</el-radio>
						<el-radio :label="2">免费</el-radio>
					</el-radio-group>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">视频介绍：</span>
					<el-input v-model="content" style="width:50%;display: none;" placeholder="请输入视频介绍"></el-input>
					<quill-editor ref="myTextEditor" v-model="content" :options="quillOption"
						style="padding-bottom: 50px;height: 300px;width: 50%;display: inline-table;margin-bottom: 60px;">
					</quill-editor>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible3 = false">取 消</el-button>
					<el-button type="primary" @click="addVideos()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 批量导入视频 -->
			<el-dialog title="批量导入视频" :visible.sync="dialogFormVisiblePl" width="80%" center="">
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">是否收费：</span>
					<el-radio-group v-model="isPrice">
						<el-radio :label="1">是</el-radio>
						<el-radio :label="2">免费</el-radio>
					</el-radio-group>
				</div>
				<div v-if="isPrice === 1" style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">集价格：</span>
					<el-input v-model="price" type="number" style="width:50%;" placeholder="请输入集价格"></el-input>
				</div>
				<div v-if="isPrice === 1" style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">集积分价格：</span>
					<el-input v-model="jifen" type="number" style="width:50%;" placeholder="请输入集积分价格"></el-input>
				</div>
				<div style="margin-bottom: 10px;display:flex;">
					<span style="width: 200px;display: inline-block;text-align: right;">封面图：</span>
					<div style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="titleImg"
							:action="$http.adornUrl('alioss/upload')"  :show-file-list="false"
							:on-success="handleAvatarSuccess5">
							<img v-if="titleImg" :src="titleImg" class="avatar"
								style="border-radius: 6px;width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon iconss"></i>
						</el-upload>
					</div>
				</div>
				<div style="margin-bottom: 10px;display:flex;align-items: center;">
					<span style="width: 200px;display: inline-block;text-align: right;">上传视频：</span>
					<div style="display: flex;flex-wrap: wrap;">
						<video v-for="(item,index) in urlList" :key="index" :src="item.url" class="avatar"
							controls="controls" style="width: 148px;height:148px;margin-right: 5px; margin-left: 5px;margin-top: 5px;border: 1px black;">您的浏览器不支持视频播放</video>
						<el-upload class="avatar-uploader" v-model="url"
							style="margin-top: 5px; margin-left: 5px;width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;"
							:action="$http.adornUrl('alioss/upload')"  :show-file-list="false"
							multiple :on-success="handleAvatarSuccessPl" :before-upload="beforeUploadVideoPl" :on-change="onFileChangePl">
							<i class="el-icon-plus avatar-uploader-icon iconss"></i>
						</el-upload>
					</div>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisiblePl = false">取 消</el-button>
					<el-button type="primary" @click="addVideosPl()">确 定</el-button>
				</div>
			</el-dialog>

			<!-- 一键同步微信短剧的结果 -->
			<el-dialog title="微信短剧同步结果" :visible.sync="wxSyncVisible" width="90%" center="">
				<div style="margin-bottom: 10px;color: #909399;font-size: 13px;">
					同步进来的剧默认是【下架 + 免费】状态，确认封面简介无误后，请在列表里手动上架、按需改价。
					只有「已过审」的集才能在小程序里播放。
				</div>
				<el-table :data="wxSyncReport" max-height="400">
					<el-table-column prop="dramaId" label="微信剧目ID" width="120"></el-table-column>
					<el-table-column prop="courseId" label="本地剧ID" width="100"></el-table-column>
					<el-table-column prop="title" label="剧名" width="200"></el-table-column>
					<el-table-column label="本地剧" width="100">
						<template slot-scope="scope">
							<el-tag v-if="scope.row.isNew==1" size="mini" type="success">新建</el-tag>
							<el-tag v-else size="mini" type="info">已存在</el-tag>
						</template>
					</el-table-column>
					<el-table-column prop="mediaCount" label="微信集数" width="100"></el-table-column>
					<el-table-column prop="inserted" label="新增集" width="90"></el-table-column>
					<el-table-column prop="updated" label="更新集" width="90"></el-table-column>
					<el-table-column prop="unchanged" label="无变化" width="90"></el-table-column>
					<el-table-column prop="auditPassed" label="已过审" width="90"></el-table-column>
					<el-table-column prop="auditPending" label="审核中" width="90"></el-table-column>
					<el-table-column prop="auditRejected" label="未过审" width="90"></el-table-column>
				</el-table>
				<div v-if="wxSyncWarnings.length > 0" style="margin-top: 15px;">
					<div style="color: #E6A23C;margin-bottom: 5px;">以下内容没能同步：</div>
					<div v-for="(item,index) in wxSyncWarnings" :key="index"
						style="color: #E6A23C;font-size: 13px;line-height: 22px;">
						{{item}}
					</div>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button type="primary" @click="wxSyncVisible = false">知道了</el-button>
				</div>
			</el-dialog>

		</el-tab-pane>
		<!-- <el-tab-pane label="视频类别" name="third">
			<div style="display: inline-block;float: right;">
				<el-button style="margin:0 0 20px 20px;" :disabled="!isAuth('mission:add')" size="mini" type="primary"
					icon="document" @click="classifyStair()">添加类别</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="classifyData.list">
				<el-table-column prop="classificationId" label="编号" width="80"></el-table-column>
				<el-table-column prop="classificationName" label="类别名称">
				</el-table-column>
				<el-table-column prop="isDelete" label="状态">
					<template slot-scope="scope">
						<span v-if="scope.row.isDelete==0">使用中</span>
						<span v-if="scope.row.isDelete==1">已下架</span>
					</template>
				</el-table-column>
				<el-table-column label="操作" width="150">
					<template slot-scope="scope">
						<el-button size="mini" type="danger" :disabled="!isAuth('mission:delete')"
							@click="deleteStair(scope.row)">删除
						</el-button>
						<el-button size="mini" type="primary" :disabled="!isAuth('mission:update')"
							@click="compile(scope.$index, scope.row)">编辑
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
					:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="classifyData.length">
				</el-pagination>
			</div>

		</el-tab-pane> -->
		<!-- 添加分类 -->
		<el-dialog title="添加类别" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">类别名称：</span>
				<el-input style="width:50%;" v-model="classificationName" type="text" placeholder="请输入类别名称">
				</el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="StairNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 修改分类 -->
		<el-dialog title="编辑类别" :visible.sync="dialogFormVisible2" center>
			<el-form :model="form">
				<el-form-item label="类别名称：" :label-width="formLabelWidth">
					<el-input v-model="form.classificationName" style="width:65%;"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible2 = false">取 消</el-button>
				<el-button type="primary" @click="CompileNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<el-dialog title="视频" :visible.sync="tableDateUrl" center width="600px">
			<video v-if="videoUrls!=''" :src="videoUrls" ref="videoRef" width="400" controls
				style="margin: 30px 70px;"></video>
			<video v-else ref="videoRef" width="400" style="margin: 30px 70px;" type="audio/wav"
				controls="controls"></video>
			<div slot="footer" class="dialog-footer">
				<el-button @click="tableDateUrl = false">取 消</el-button>
			</div>
		</el-dialog>
		<!-- 批量修改 -->
		<el-dialog title="批量修改" :visible.sync="dialogFormVisiblePltk" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">集价格：</span>
				<el-input v-model="price" type="number" style="width:50%;" placeholder="请输入集价格,0为不收费"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">集积分价格：</span>
				<el-input v-model="jifen" type="number" style="width:50%;" placeholder="请输入集积分价格,0为不收费"></el-input>
			</div>
			<div style="margin-bottom: 10px;display:flex;">
				<span style="width: 200px;display: inline-block;text-align: right;">封面图：</span>
				<div
					style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
					<el-upload class="avatar-uploader" v-model="titleImg"
						:action="$http.adornUrl('alioss/upload')"  :show-file-list="false"
						:on-success="handleAvatarSuccess5">
						<img v-if="titleImg" :src="titleImg" class="avatar"
							style="border-radius: 6px;width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon iconss"></i>
					</el-upload>
				</div>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">视频介绍：</span>
				<el-input v-model="content" style="width:200px;display: none;" placeholder="请输入视频介绍"></el-input>
				<quill-editor ref="myTextEditor" v-model="content" :options="quillOption"
					style="padding-bottom: 50px;height: 300px;width: 50%;display: inline-table;margin-bottom: 60px;">
				</quill-editor>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisiblePltk = false">取 消</el-button>
				<el-button type="primary" @click="updateVideos()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 批量上下架 -->
		<el-dialog title="批量上下架" :visible.sync="dialogFormVisiblePlsxj" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">修改状态：</span>
				<el-radio-group v-model="isPrice">
					<el-radio :label="1">上架</el-radio>
					<el-radio :label="2">下架</el-radio>
				</el-radio-group>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisiblePlsxj = false">取 消</el-button>
				<el-button type="primary" @click="updateVideosSxj()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 收入统计 -->
		<el-dialog title="收入统计" :visible.sync="dialogVisibleTj" width="90%" center="">
			<el-row>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">总收入 </div>
							<div class="text_color">
								<span>{{tongjidata.sumMoney ? tongjidata.sumMoney : 0}}</span>元</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">今日收入</div>
							<div class="text_color">
								<span>{{tongjidata.dayMoney ? tongjidata.dayMoney : 0 }}</span>元</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">本月收入 </div>
							<div class="text_color">
								<span>{{tongjidata.monthMoney ? tongjidata.monthMoney : 0}}</span>元</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">本年收入</div>
							<div class="text_color"><span>{{tongjidata.yearMoney ? tongjidata.yearMoney : 0}}</span>元
							</div>
						</div>
					</div>
				</el-col>

			</el-row>
			<div style="display: inline-block;">
				<el-select v-model="flag" style="width:150px;margin-left: 10px;" @change="chaxunTj">
					<el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;
				<el-date-picker style="width: 200px;margin-left: 10px;" v-model="info.stockDate" align="right"
					type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间"
					@change="chaxunTj">
				</el-date-picker>
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="chaxunTj">
					查询</el-button>

			</div>
			<el-table v-loading="tableDataLoadingSr" :data="tongjiListdata.list">

				<el-table-column prop="ordersId" label="编号" width="80" fixed="left"></el-table-column>
				<el-table-column prop = "userName" label = "购买用户" width = "120" align="center">
					<template slot-scope = "scope">
						<span style = "color: #4f9dec;cursor: pointer;" @click = "updates(scope.row)">
							{{ scope.row.userName}}
						</span>
					</template>
				</el-table-column>
				<el-table-column prop="ordersNo" label="订单编号" width="200"></el-table-column>
				<el-table-column prop = "payWay" label = "支付方式"  width = "120">
					<template slot-scope = "scope">
						<span v-if = "scope.row.payWay == null">暂无</span>
						<span v-if = "scope.row.payWay == 1">微信APP</span>
						<span v-if = "scope.row.payWay == 2">微信公众号</span>
						<span v-if = "scope.row.payWay == 3">微信小程序</span>
						<span v-if = "scope.row.payWay == 4">支付宝</span>
						<span v-if = "scope.row.payWay == 5">会员免费</span>
						<span v-if = "scope.row.payWay == 6">零钱</span>
					</template>
				</el-table-column>
				<el-table-column prop="payMoney" label="支付金额"></el-table-column>
				<el-table-column prop="createTime" label="购买时间"></el-table-column>
			</el-table>
			<div style="color: #B94A48;font-size: 20px;margin-top: 10px;display: inline-block;">
				本页累计收入统计：{{totalMoney.toFixed(2)}}元； </div>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChangeTjlist" @current-change="handleCurrentChangeTjlist"
					:page-sizes="[10, 20, 30, 40, 100, 200, 500]" :page-size="limitS" :current-page="pageS"
					layout="total,sizes, prev, pager, next,jumper" :total="tongjiListdata.totalCount">
				</el-pagination>
			</div>
		</el-dialog>
	</el-tabs>
</template>

<script>
	import {
		quillEditor
	} from 'vue-quill-editor'
	import 'quill/dist/quill.core.css'
	import 'quill/dist/quill.snow.css'
	import 'quill/dist/quill.bubble.css'
	import quillConfig from '../locality/quill-config.js'
	// import videojs from "video.js";
	// import "video.js/dist/video-js.css";
	import Hls from 'hls.js';
	export default {
		components: {
			quillEditor
		},
		data() {
			return {
				// 一键同步微信短剧
				wxSyncLoading: false,
				wxSyncVisible: false,
				wxSyncReport: [],
				wxSyncWarnings: [],
				openValue: 1,
				closeValue: 0,
				openValue1: 1,
				closeValue1: 2,
				contentT: '',
				courseType: 1, //资源类型 1:资源 2:链接
				isRecommend: 0, //是否推荐 1：推荐 0：不推荐
				statusType: 1, //上下架 1：上架 2：下架
				remark: '', //资源网盘地址
				title: '', //资源列表名称
				titleImg: '', //资源列表图片

				img: [], //详情图合集
				bannerImg: [], //轮播图
				privilege: [], //标签集合
				bq: '', //标签
				price: '', //价格
				jifen: 0,
				payNum: '', //购买次数
				viewCounts: 0, //播放量
				details: '', //资源介绍
				courseId: '', //资源id
				curriList: {}, //资源详情
				url: '', //视频链接
				isPrice: 1, //是否收费
				goodNum: '', //点赞数
				videoId: '',
				quillOption: quillConfig,
				fenleiId: '',
				fenleiIdT: '',
				fenleiId1: '',
				fenleiId2: '',
				limit: 10,
				page: 1,
				limitS: 10,
				pageS: 1,
				goods:'',
				goodsList:[
					{
						classificationName: '全部',
						classificationId: ''
					},
					{
						classificationName: '推荐',
						classificationId: 1
					}, {
						classificationName: '不推荐',
						classificationId: 2
					}
				],
				phone: '',
				status: 0,
				statesnum: [{ //资源管理分类
					classificationName: '全部',
					classificationId: ''
				}],
				statesnum1: [],
				statesnum2: [],
				isRecommends: -1,
				isRecommendNum: [ //资源管理分类
					{
						classificationName: '全部',
						classificationId: -1
					},
					{
						classificationName: '推荐',
						classificationId: 1
					}, {
						classificationName: '普通',
						classificationId: 0
					}
				],
				statuss: 0,
				statusNum: [ //上下架分类
					{
						classificationName: '全部',
						classificationId: 0
					},
					{
						classificationName: '上架',
						classificationId: 1
					}, {
						classificationName: '下架',
						classificationId: 2
					}
				],
				classificationId: '', //分类id
				content: '', //资源搜索标题 拒绝理由 视频简介
				classificationName: '', //分类名称
				formLabelWidth: '200px',
				activeName: 'first',
				dialogVisible: false,
				tableDataLoading: false,
				dialogFormVisible: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				tableData: {}, //资源信息集合
				form: {
					classificationId: '',
					classificationName: ''
				},
				classifyData: [], //分类管理集合
				xg: 0,
				classifyId: 0,
				userIds: [], //用户列表
				nickName: '',
				percentage: 0,
				titles: '',
				types: '',
				tableDataLoadingUrl: false,
				dialogFormVisiblePl: false,
				urlList: [],
				show: false,
				// ------
				tableDateUrl: false,
				videoUrls: '',
				checkBoxData: [], //多选框选择的值
				dialogFormVisiblePltk: false,
				dialogFormVisiblePlsxj: false,

				dialogVisibleTj:false,
				tongjidata:{},
				tongjiListdata:{},
				info: {
					stockDate: this.getNowTime(), //日期
				},
				flag:3,
				flags: [{
					value: 1,
					label: '按天查询'
				}, {
					value: 2,
					label: '按月查询'
				}, {
					value: 3,
					label: '按年查询'
				}],
				totalMoney: 0,
				tableDataLoadingJj: false,
				tableDataLoadingSr: false,
				fileList: [],
			}
		},
		methods: {
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date();
				var year = now.getFullYear(); //得到年份
				var month = now.getMonth(); //得到月份
				var date = now.getDate(); //得到日期
				var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
				var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
				var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
				month = month + 1;
				month = month.toString().padStart(2, "0");
				date = date.toString().padStart(2, "0");
				var defaultDate = `${year}-${month}-${date}`;
				return defaultDate;
				this.$set(this.info, "stockDate", defaultDate);
			},
			// 详情跳转
			updates(row) {
				if (row.userId) {
					this.$router.push({
						path: '/userDetail',
						query: {
							userId: row.userId
						}
					})
				}

			},
			handleSizeChange(val) {
				this.limit = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			handleSizeChange2(val) {
				this.limit = val
				this.classifySelect()
			},
			handleCurrentChange2(val) {
				this.page = val
				this.classifySelect()
			},
			handleSizeChange3(val) {
				this.limitS = val
				this.curriculumList(this.courseId);
			},
			handleCurrentChange3(val) {
				this.pageS = val
				this.curriculumList(this.courseId);
			},

			handleSizeChangeTjlist(val) {
				this.limitS = val
				this.shourutongjiList(this.courseId);
			},
			handleCurrentChangeTjlist(val) {
				this.pageS = val
				this.shourutongjiList(this.courseId);
			},
			handleSizeChange5(val) {
				this.limit = val
				this.userClass();
			},
			handleCurrentChange5(val) {
				this.page = val
				this.userClass();
			},
			// tabs切换
			handleClick(tab, event) {
				if (tab._props.label == '视频信息') {
					this.page = 1
					this.limit = 10
					this.dataSelect()
				}
				if (tab._props.label == '视频类别') {
					this.page = 1
					this.limit = 10
					this.classifySelect()
				}
			},
			// 查询资源列表
			select() {
				this.page = 1
				this.limit = 10
				this.dataSelect()
			},
			// 重置资源列表
			cleans() {
				this.phone = ''
				this.status = 0
				this.contentT = ''
				this.fenleiIdT = ''
				this.isRecommends = -1
				this.statuss = ''
				this.dataSelect()
			},
			// 添加资源列表
			tianjia() {
				this.titles = '添加资源'
				this.xg = 0
				this.fenleiId1 = '';
				this.privilege = []
				this.img = []
				this.bannerImg = []
				this.details = '';
				this.viewCounts = 0
				this.isPrice = 2
				this.payNum = '';
				this.price = '';
				this.jifen = '';
				this.fenleiId2 = '';
				this.title = '';
				this.titleImg = '';
				this.courseId = ''
				this.remark = ''
				this.courseType = 1
				this.dialogFormVisible1 = true
				this.classifySelect1()

			},
			// 修改资源列表
			xiugai(row) {
				this.titles = '修改资源'
				this.xg = 1
				console.log(row)
				this.fenleiId1 = row.classifyId;
				if (row.bannerId) {
					this.fenleiId2 = row.bannerId;
				}
				if (row.courseLabel) {
					this.privilege = row.courseLabel.split(',');
				}
				if (row.img) {
					this.img = row.img.split(',');
				}
				if (row.bannerImg) {
					this.bannerImg = row.bannerImg.split(',');
				} else {
					this.bannerImg = []
				}
				this.courseType = row.courseType
				this.remark = row.remark
				this.isRecommend = row.isRecommend
				this.statusType = row.status
				this.details = row.details;
				this.viewCounts = row.viewCounts;

				this.payNum = row.payNum;
				if (row.isPrice) {
					this.isPrice = row.isPrice
				} else {
					this.isPrice = 2
				}
				this.price = row.price;
				this.jifen = row.jifen;
				this.title = row.title;
				this.titleImg = row.titleImg;
				this.courseId = row.courseId
				this.dialogFormVisible1 = true
				this.curriculumList(row.courseId)
				this.classifySelect1()
			},
			// 封面图片上传
			handleAvatarSuccess5(file) {
				this.titleImg = file.data;
			},
			// 详情图片上传
			handleRemove(file) {
				console.log(file, this.img)
				this.img.push(file.data);
			},
			// 轮播图上传
			handleRemoveB(file) {
				console.log(file, this.bannerImg)
				this.bannerImg.push(file.data);
			},

			// 上传文档
			handleAvatarSuccessWd(file) {
				this.remark = file.data;
			},
			// 删除标签
			dels2(index) {
				this.privilege.splice(index, 1);
				console.log(this.privilege)
			},
			// 添加标签
			btnTj() {
				if (this.bq == '' || this.bq == ' ') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入标签',
						type: 'warning'
					});
					return
				} else {
					console.log('this.bq', this.bq)
					this.bq.replace(/[, ]/g,'，')
					this.privilege.push(this.bq)
					this.bq = ''
				}
			},
			// 确认添加列表
			addDetails() {
				if (this.courseType == 2) {
					if (this.remark == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入链接备注',
							type: 'warning'
						})
						return
					}
				}
				// if (this.fenleiId1 === '' || this.fenleiId1 == null) {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请选择类别',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				// if (this.fenleiId2 === ''||this.fenleiId2 ==null) {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请选择分类',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				if (this.title == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入名称',
						type: 'warning'
					})
					return
				}
				if (this.titleImg == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传封面图',
						type: 'warning'
					})
					return
				}

				// if (this.bannerImg.length == 0) {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请上传轮播图',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				// if (this.img.length == 0) {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请上传详情图',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				if (this.price === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入购买剧价格',
						type: 'warning'
					})
					return
				}
				if (this.payNum === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入购买次数',
						type: 'warning'
					})
					return
				}
				if (this.viewCounts === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入播放量',
						type: 'warning'
					})
					return
				}
				if (this.privilege.length == 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '添加标签',
						type: 'warning'
					})
					return
				}
				if (this.xg == 0) {
					var url = 'course/insertCourse'
				} else {
					var url = 'course/updateCourse'
				}
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornData({
						'classifyId': this.fenleiId1,
						'courseLabel': this.privilege.toString(),
						'details': this.details,
						'img': this.img.toString(),
						'bannerImg': this.bannerImg.toString(),
						'payNum': this.payNum,
						'price': this.price,
						'jifen': this.jifen,
						'viewCounts': this.viewCounts,

						'title': this.title,
						'titleImg': this.titleImg,
						'msgUrl': '',
						'courseId': this.courseId,
						'courseType': this.courseType,
						'isRecommend': this.isRecommend,
						'status': this.statusType,
						'remark': this.remark,
						'bannerId': this.fenleiId2,
						'isPrice': this.isPrice,
					})
				}).then(({
					data
				}) => {
					var that = this
					if (data.code == 0) {

						that.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								that.dialogFormVisible1 = false
								that.fenleiId1 = '';
								that.fenleiId2 = '';
								that.privilege = [];
								that.img = [];
								that.bannerImg = []
								that.details = '';
								that.payNum = '';
								that.viewCounts = 0
								that.isPrice = 2
								that.price = '';
								that.jifen = '';
								that.title = '';
								that.remark = '';
								that.titleImg = '';
								that.dataSelect()
							}
						})
					} else {
						that.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {}
						})
					}

				})
			},
			// select选择事件
			animeDat(state) {
				this.page = 1
				console.log(state)
				this.dataSelect()
			},
			animeDat1(state) {
				this.fenleiId2 = state
			},
			animeDat2(state) {
				this.page = 1
				this.isRecommends = state
				this.dataSelect()
			},
			animeDat3(state) {
				this.page = 1
				this.statuss = state
				this.dataSelect()
			},
			// 获取派单数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('course/selectCourse'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'classifyId': this.fenleiIdT,
						'title': this.contentT,
						'isRecommend': this.isRecommends,
						'status': this.statuss
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data

					this.tableData = returnData
					console.log(this.tableData)
				})
			},
			// 删除资源列表
			deles(row) {
				let delid = row.courseId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`course/updateDelete/?id=${delid}`),
						method: 'get',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 查看资源详情
			refund1(row) {
				this.pageS = 1
				this.types = row.courseType
				this.courseId = row.courseId
				this.curriculumList(this.courseId);
				this.dialogVisible = true

			},

			// 查询
			chaxun(){
				this.pageS = 1
				this.curriculumList(this.courseId);
			},
			// 获取资源详情列表
			curriculumList(id) {
				this.tableDataLoadingJj = true
				this.$http({
					url: this.$http.adornUrl('course/selectCourseById'),
					method: 'get',
					params: this.$http.adornParams({
						'id': id,
						'page': this.pageS,
						'limit': this.limitS,
						'good':this.goods
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.tableDataLoadingJj = false
						this.curriList = data.data
						this.remark = data.data.list[0].videoUrl
						this.videoId = data.data.list[0].courseDetailsId
					}

				})
			},
			// 添加视频
			addVideo() {
				this.title = ''
				this.url = ''
				this.videoId = ''
				this.price = ''
				this.jifen = ''
				this.titleImg = ''
				this.goodNum = ''
				this.content = ''
				this.isPrice = 1
				this.dialogFormVisible3 = true
			},
			// 修改视频
			compileVideo(row) {
				this.title = row.courseDetailsName
				this.url = row.videoUrl
				this.videoId = row.courseDetailsId
				this.isPrice = row.isPrice
				this.price = row.price
				this.jifen = row.jifen
				this.titleImg = row.titleImg
				this.goodNum = row.goodNum
				this.content = row.content

				this.dialogFormVisible3 = true
			},
			// 确认添加视频
			addVideos() {
				if (this.title == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入视频标题',
						type: 'warning'
					})
					return
				}
				if (this.url == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传视频',
						type: 'warning'
					})
					return
				}
				if (this.isPrice == 1) {
					if (Number(this.price) === '' || Number(this.price) <= 0) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '集价格不能为空或不能小于0',
							type: 'warning'
						})
						return
					}
				} else {
					if (Number(this.price) === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '集价格不能为空',
							type: 'warning'
						})
						return
					}
				}
				if (Number(this.goodNum)<0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '点赞数不能小于0',
						type: 'warning'
					})
					return
				}
				if (this.videoId == '') {
					var urls = 'courseDetails/insertCourseDetails'
				} else {
					var urls = 'courseDetails/updateCourseDetails'
				}
				this.$http({
					url: this.$http.adornUrl(urls),
					method: 'post',
					data: this.$http.adornData({
						'courseId': this.courseId,
						'courseDetailsName': this.title,
						'videoUrl': this.url,
						'courseDetailsId': this.videoId,
						'isPrice': this.isPrice,
						'price': this.price,
						'jifen': this.jifen,
						'titleImg': this.titleImg,
						'goodNum': this.goodNum,
						'content': this.content,
					})
				}).then(({
					data
				}) => {
					console.log('添加资源视频', data)
					if (data.code == 0) {
						this.$message({
							message: '添加成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.title = ''
								this.url = ''
								this.videoId = ''
								this.dialogFormVisible3 = false
								this.curriculumList(this.courseId);
							}
						})
					} else {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: data.msg,
							type: 'warning'
						})
					}

				})
			},
			// 确认添加视频
			addVideosPl() {
				if (this.urlList.length === 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传至少一个视频',
						type: 'warning'
					});
					return;
				}
				if (this.isPrice == 1) {
					if (Number(this.price) === '' || Number(this.price) <= 0) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '集价格不能为空或不能小于0',
							type: 'warning'
						})
						return
					}
				} else {
					if (this.price === '') {
						this.price = 0;
						return
					}
				}

				this.$http({
					url: this.$http.adornUrl('courseDetails/insertCourseDetailsMulti'),
					method: 'post',
					data: this.$http.adornData({
						'courseId': this.courseId,
						'courseDetailsName': this.title,
						'videoUrls': this.urlList,
						'isPrice': this.isPrice,
						'price': this.price,
						'jifen': this.jifen,
						'titleImg': this.titleImg,
						'goodNum': this.goodNum,
					})
				}).then(({
					data
				}) => {
					console.log('添加资源视频', data)
					if (data.code == 0) {
						this.$message({
							message: '添加成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.title = ''
								this.urlList = []
								this.url = ''
								this.videoId = ''
								this.titleImg = ''
								this.goodNum = ''
								this.dialogFormVisiblePl = false
								this.curriculumList(this.courseId);
							}
						})
					} else {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: data.msg,
							type: 'warning'
						})
					}

				})
			},
			// 删除视频
			deleteVideo(row) {
				let delid = row.courseDetailsId.toString()
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`courseDetails/deleteCourseDetails?ids=${delid}`),
						method: 'post',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.curriculumList(this.courseId)
								}
							})
						} else {
							this.$notify({
								title: '提示',
								duration: 1800,
								message: data.msg,
								type: 'warning'
							})
						}
					})
				}).catch(() => {})
			},
			// 视频上传
			handleAvatarSuccess3(file) {
				this.url = file.data;
				this.percentage = 0
			},
			onFileChangePl(){
				if (this.loading && this.fileList.length === 0) {
					this.loading.close();
				}
			},
			// 限制上传格式
			beforeUploadVideo(file) {

				const isLt10M = file.size / 1024 / 1024 < 1024;
				if (['video/mp4', 'video/ogg', 'video/flv', 'video/avi', 'video/wmv', 'video/rmvb'].indexOf(file.type) == -
					1) {
					this.$message.error('请上传正确的视频格式');
					return false;
				}
				if (!isLt10M) {
					this.$message.error('上传视频大小不能超过1G哦!');
					return false;
				}
			},
			// 限制上传格式
			beforeUploadVideoPl(file) {

				const isLt10M = file.size / 1024 / 1024 < 1024;
				if (['video/mp4', 'video/ogg', 'video/flv', 'video/avi', 'video/wmv', 'video/rmvb'].indexOf(file.type) == -
					1) {
					this.$message.error('请上传正确的视频格式');
					return false;
				}
				if (!isLt10M) {
					this.$message.error('上传视频大小不能超过1G哦!');
					return false;
				}
				this.fileList.push(file)
				this.loading = this.$loading({
					lock: true,
					text: '文件上传中...',
					spinner: 'el-icon-loading',
					background: 'rgba(0, 0, 0, 0.7)'
				});
			},
			beforeUpload(file) {
				this.url = ''
				this.percentage = Math.ceil(file.percent)
			},
			// 批量导入视频弹框
			addVideoPl() {
				this.dialogFormVisiblePl = true
			},
			handleAvatarSuccessPl(file) {
				console.log('多个视频', file)
				this.urlList.push({"url": file.data, "fileName": file.fileName})
				this.fileList.pop();
			},
			// 分类管理
			classifySelect() {
				this.$http({
					url: this.$http.adornUrl('courseClassification/selectCourseClassification'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'classificationName': ''
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.classifyData = returnData
					this.statesnum1 = data.data.list
					if (this.statesnum.length == 1) {
						for (var i in data.data.list) {
							this.statesnum.push(data.data.list[i])
						}
					}
				})
			},
			classifySelect1() {
				this.$http({
					url: this.$http.adornUrl('banner/selectBannerList'),
					method: 'get',
					params: this.$http.adornParams({
						'state': -1,
						'classify': 2,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.classifyData = returnData
					this.statesnum2 = data.data

				})
			},
			// 添加分类弹框
			classifyStair() {
				this.dialogFormVisible = true
			},
			// 添加分类确定
			StairNoticeTo() {
				if (this.classificationName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入分类名称',
						type: 'warning'
					})
					return
				}
				this.$http({
					url: this.$http.adornUrl('courseClassification/insertCourseClassification'),
					method: 'post',
					data: this.$http.adornData({
						'classificationName': this.classificationName
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible = false
					this.$message({
						message: '分类添加成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.classificationName = ''
							this.classifySelect()
						}
					})
				})
			},
			// 修改分类
			compile(index, rows) {
				this.dialogFormVisible2 = true
				this.form.classificationId = rows.classificationId
				this.form.classificationName = rows.classificationName
			},
			// 修改分类确定
			CompileNoticeTo() {
				if (this.form.classificationName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入分类名称',
						type: 'warning'
					})
					return
				}
				this.$http({
					url: this.$http.adornUrl('courseClassification/updateCourseClassification'),
					method: 'post',
					data: this.$http.adornData({
						'classificationId': this.form.classificationId,
						'classificationName': this.form.classificationName
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible2 = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.classifySelect()
						}
					})
				})
			},
			//删除分类
			deleteStair(row) {
				let delid = row.classificationId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`courseClassification/updateDelete?id=${delid}`),
						method: 'get',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.name = ''
								this.classifySelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 详情跳转
			// updates (row) {
			// 	this.$router.push({path: '/userDetail', query: {userId: row.userId}})
			// },
			// 删除详情图
			dels(index) {
				this.img.splice(index, 1);
				console.log(this.img)
			},
			// 删除轮播图
			delsB(index) {
				this.bannerImg.splice(index, 1);
				console.log(this.bannerImg)
			},

			// 推荐与否
			change(row) {
				this.$http({
					url: this.$http.adornUrl(`course/updateCourse`),
					method: 'post',
					data: this.$http.adornData({
						'isRecommend': row.isRecommend,
						'courseId': row.courseId,

						'classifyId': row.classifyId,
						'courseLabel': row.courseLabel,
						'details': row.details,
						'img': row.img.toString(),
						'payNum': row.payNum,
						'price': row.price,
						'jifen': row.jifen,

						'viewCounts': row.viewCounts,
						'title': row.title,
						'titleImg': row.titleImg,
						'msgUrl': '',
						'courseType': row.courseType,
						'status': row.status,
						'remark': row.remark,
						'bannerId': row.bannerId,
						// 'isPrice':row.isPrice,
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
			// 上下架
			change1(status, courseId) {
				this.$http({
					url: this.$http.adornUrl(`course/updateCourse`),
					method: 'get',
					// data: this.$http.adornData({
					params: this.$http.adornParams({
						'status': status,
						'courseId': courseId
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
			handleSuccess(file) {
				console.log(file)
				this.url = file.data;
				console.log(file.data, this.url)
				this.tableDataLoadingUrl = false
			},
			handlePreview(file) {
				this.tableDataLoadingUrl = true
			},
			// 验证只能上传音频
			beforeAvatarUpload(file) {
				var testmsg = file.name.substring(file.name.lastIndexOf('.') + 1)
				const extension = testmsg === 'mp3'
				if (!extension) {
					this.$message({
						message: "上传文件只能是mp3格式！",
						type: 'error'
					})
				}
				return extension;
			},
			// 是否免费
			changeD(row) {
				if (row.price != null && row.jifen != null) {
					var price = row.price
					var jifen = row.jifen
				} else {
					var price = 0
					var jifen = 0
				}

				if (row.isPrice == 1 && price == 0 && row.jifen == 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '当前集价格为空或者为0，设置为收费前，请先设置集价格',
						type: 'warning'
					})
					this.curriculumList(this.courseId)
					return
				}
				this.$http({
					url: this.$http.adornUrl('courseDetails/updateCourseDetails'),
					method: 'post',
					data: this.$http.adornData({
						'courseId': row.courseId,
						'courseDetailsName': row.courseDetailsName,
						'videoUrl': row.videoUrl,
						'courseDetailsId': row.courseDetailsId,
						'isPrice': row.isPrice,
						'price': row.price,
						'jifen': row.jifen,
						'titleImg': row.titleImg,
						'goodNum': row.goodNum,
						'content': row.content,
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
								this.curriculumList(this.courseId)
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {
								this.curriculumList(this.courseId)
							}
						})
					}

				})
			},
			// 是否推荐
			changeT(row) {
				if (row.isPrice == 1 && row.good == 1) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '推荐视频应为免费观看，请先设置视频为免费',
						type: 'warning'
					})
					this.curriculumList(this.courseId)
					return
				}
				this.$http({
					url: this.$http.adornUrl('courseDetails/updateCourseDetails'),
					method: 'post',
					data: this.$http.adornData({
						'courseDetailsId': row.courseDetailsId,
						'good': row.good,
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
								this.curriculumList(this.courseId)
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {
								this.curriculumList(this.courseId)
							}
						})
					}

				})
			},
			// 是否完结
			changeW(row) {
				this.$http({
					url: this.$http.adornUrl('course/updateCourse'),
					method: 'post',
					data: this.$http.adornData({
						// params: this.$http.adornParams({
						'isOver': row.isOver,
						'courseId': row.courseId
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
			// 是否展示同步资源按钮
			xianshi() {
				this.$http({
					url: this.$http.adornUrl('common/type/247'),
					method: 'get',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						if (data.data.value == '是') {
							this.show = true
						}
					}
				})
			},
			// 一键同步微信短剧：拉全量微信媒资，自动建剧建集并回填媒资ID
			syncWechatDrama() {
				this.$confirm(`将拉取微信媒资库里的全部剧集，自动建剧、建集并回填媒资ID。已存在的剧只补媒资ID，不会覆盖已配好的价格和封面。确定同步?`, '一键同步微信短剧', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.wxSyncLoading = true
					this.$http({
						url: this.$http.adornUrl('course/syncWechatDrama'),
						method: 'get',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						this.wxSyncLoading = false
						if (data.code == 0) {
							this.wxSyncReport = data.data || []
							this.wxSyncWarnings = data.warnings || []
							this.wxSyncVisible = true
							this.$message({
								message: data.msg,
								type: 'success',
								duration: 3000
							})
							this.dataSelect()
						} else {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 5000
							})
						}
					}).catch(() => {
						this.wxSyncLoading = false
						this.$message({
							message: '同步失败，请查看后端日志',
							type: 'error',
							duration: 5000
						})
					})
				}).catch(() => {})
			},
			// 同步资源
			tongbu() {
				this.$confirm(`确定要同步资源?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('course/synCourse'),
						method: 'get',
						params: this.$http.adornParams({})
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
				}).catch(() => {})
			},

			playVideo(url) {
				this.videoUrls = ''
				if (url.indexOf("m3u8") != -1) {
					var hls = new Hls();
					hls.loadSource(url);
					hls.attachMedia(this.$refs.videoRef);
					hls.on(Hls.Events.MANIFEST_PARSED, function() {
						this.$refs.videoRef.play();
					});
					this.tableDateUrl = true
				} else {
					this.tableDateUrl = true
					this.videoUrls = url;
				}
			},
			// 多选
			changeFun(val) {
				console.log('val---------', val)
				this.checkBoxData = val
			},
			// 批量修改弹框
			transferClcik(id) {
				this.price = ''
				this.jifen = ''
				this.content = ''
				this.titleImg = ''
				this.dialogFormVisiblePltk = true

			},
			// 批量修改
			updateVideos() {
				if (this.price === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入集价格',
						type: 'warning'
					})
					return
				}
				// if (Number(this.price)<0) {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '集价格不能小于0',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				var ids = this.checkBoxData.map(item => {
					return item.courseDetailsId
				})
				var datas = {
					ids:ids.toString(),
					price:this.price,
					jifen:this.jifen,
					titleImg:this.titleImg,
					content:this.content
				}
				if(this.titleImg===''){
					datas.titleImg = null
				}
				if(this.content===''){
					datas.content = null
				}
				this.$http({
					url: this.$http.adornUrl('course/updateCourseDetails'),
					method: 'post',
					params: this.$http.adornParams(datas)
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.curriculumList(this.courseId)
								this.dialogFormVisiblePltk = false
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
			},
			// 批量上下架弹框
			shangxiajiaClcik() {
				this.isPrice = ''
				this.dialogFormVisiblePlsxj = true
			},
			// 批量上下架
			updateVideosSxj() {
				var ids = this.checkBoxData.map(item => {
					return item.courseId
				})
				this.$http({
					url: this.$http.adornUrl('course/updateCourseStatus'),
					method: 'post',
					params: this.$http.adornParams({
						'ids': ids.toString(),
						'status': this.isPrice,
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
								this.dialogFormVisiblePlsxj = false
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
			},
			// 批量删除
			deleteClcik() {
				var ids = this.checkBoxData.map(item => {
					return item.courseId
				})
				this.$confirm(`确定要批量删除信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('course/deleteCourseByIds'),
						method: 'post',
						params: this.$http.adornParams({
							'ids': ids.toString(),
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
				}).catch(() => {})
			},
			// 批量删除剧集
			deleteClcikJj() {
				var ids = this.checkBoxData.map(item => {
					return item.courseDetailsId
				})
				this.$confirm(`确定要批量删除剧集?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('course/deleteCourseDetailsByIds'),
						method: 'post',
						params: this.$http.adornParams({
							'ids': ids.toString(),
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
									this.curriculumList(this.courseId)
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
				}).catch(() => {})
			},
			// 查看收入统计
			shouru(row) {
				this.pageS = 1
				this.flag = 3
				this.types = row.courseType
				this.courseId = row.courseId
				this.shourutongji();
				this.shourutongjiList()
				this.dialogVisibleTj = true

			},
			// 获取资源收入统计
			shourutongji(id) {
				this.$http({
					url: this.$http.adornUrl('order/selectCourseOrdersMoneyCount'),
					method: 'get',
					params: this.$http.adornParams({
						'courseId': this.courseId,
						// 'page': this.pageS,
						// 'limit': this.limitS,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.tongjidata = data.data
					}
				})
			},
			// 获取资源收入统计列表
			shourutongjiList(id) {
				this.totalMoney = 0
				this.tableDataLoadingSr = true
				this.$http({
					url: this.$http.adornUrl('order/selectOrders'),
					method: 'get',
					params: this.$http.adornParams({
						'courseId': this.courseId,
						'page': this.pageS,
						'limit': this.limitS,
						'time': this.info.stockDate,
						'flag': this.flag,
						'status':1
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.tableDataLoadingSr = false
						for (var i in data.data.list) {
							if (data.data.list[i].payMoney) {
								this.totalMoney = this.totalMoney + Number(data.data.list[i].payMoney)
							}
						}
						this.tongjiListdata = data.data
					}
				})
			},
			chaxunTj(){
				this.pageS = 1
				this.shourutongjiList()
			}

		},
		mounted() {
			this.classifySelect()
			this.classifySelect1()
			this.dataSelect()
			this.xianshi()

		}
	}
</script>

<style>
	.el-dialog {
		/* width: 65% !important; */
	}

	.imgs {
		position: relative;
		border-radius: 6px;
		width: 148px;
		height: 148px;
		margin-right: 10px;
		display: inline-block;
	}

	.dels {
		position: absolute;
		top: 0;
		left: 0;
		display: none;
	}

	.dels .el-icon-delete {
		line-height: 148px;
		padding-left: 58px;
		font-size: 25px;
		color: #fff;
	}

	.imgs:hover .dels {
		width: 100%;
		height: 100%;
		background: #000;
		display: block;
		opacity: 0.5;
	}

	.bqList {
		padding: 4px 14px;
		margin: 4px;
		border: 1px solid #efefef;
		font-size: 12px;
		color: #999;
		border-radius: 4px;
		margin-right: 15px;
	}

	.delss {
		display: none;
		position: relative;
	}

	.delss .el-icon-delete {
		position: absolute;
		top: 0;
	}

	.bqList:hover .delss {
		display: initial;
		opacity: 0.5;

	}

	.tj {
		padding: 4px;
		margin: 4px;
		font-size: 12px;
		border: 1px solid #ccc;
		border-radius: 4px;
	}

	.btns {
		margin: 4px 0 !important;
	}


	.video-box {
		width: 100%;
		max-width: 500px;
		max-height: 500px;
	}

	::v-deep .video-js .vjs-big-play-button {
		top: 50%;
		left: 50%;
		transform: translate(-50%, -50%);
	}
	.box {
		padding: 44px;
		border: 1px solid #eee;
		margin: 15px 10px;
	}

	.box_num {
		font-size: 14px;
		color: #66b1ff;
	}

	.box_num .box_color {
		color: #333;
		font-size: 14px;
		margin-bottom: 15px;
	}

	.box_num div span {
		font-size: 20px;
		margin-left: 5px;
	}

	.text_color {
		color: #4f9dec;
	}

	.text_color span {
		margin-right: 5px;
	}
</style>
