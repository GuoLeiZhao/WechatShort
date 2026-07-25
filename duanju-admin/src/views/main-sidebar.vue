<template>
  <aside class="site-sidebar" :class="'site-sidebar--' + sidebarLayoutSkin">
    <div class="site-sidebar__inner">
      <el-menu
        :default-active="menuActiveName || 'home'"
        :collapse="sidebarFold"
        :collapseTransition="false"
        class="site-sidebar__menu">
       <!-- <el-menu-item index="home" @click="$router.push({ name: 'home' })">
          <icon-svg name="shuju" class="site-sidebar__menu-icon"></icon-svg>
          <span slot="title">数据中心</span>
        </el-menu-item>
		<el-menu-item index="userList" @click="$router.push({ name: 'userList' })">
			<icon-svg name="yonghul" class="site-sidebar__menu-icon"></icon-svg>
			<span slot="title">用户中心</span>
		</el-menu-item>
		<el-menu-item index="financeList" @click="$router.push({ name: 'financeList' })">
			<icon-svg name="caiwu" class="site-sidebar__menu-icon"></icon-svg>
			<span slot="title">财务中心</span>
		</el-menu-item>
		<el-menu-item index="message" @click="$router.push({ name: 'message' })">
			<icon-svg name="xiaoxi" class="site-sidebar__menu-icon"></icon-svg>
			<span slot="title">消息中心</span>
		</el-menu-item>
		<el-menu-item index="mission" @click="$router.push({ name: 'mission' })">
			<icon-svg name="renwu" class="site-sidebar__menu-icon"></icon-svg>
			<span slot="title">任务中心</span>
		</el-menu-item>
		<el-menu-item index="taskConfig" @click="$router.push({ name: 'taskConfig' })">
			<icon-svg name="renwu" class="site-sidebar__menu-icon"></icon-svg>
			<span slot="title">任务配置</span>
		</el-menu-item> -->
		<!-- <el-menu-item index="missionsye" @click="$router.push({ name: 'missionsye' })">
			<icon-svg name="pingtai" class="site-sidebar__menu-icon"></icon-svg>
			<span slot="title">系统任务</span>
		</el-menu-item> -->
		<!-- <el-menu-item index="materialsList" @click="$router.push({ name: 'materialsList' })">
			<icon-svg name="xinxi" class="site-sidebar__menu-icon"></icon-svg>
			<span slot="title">好物圈</span>
		</el-menu-item> -->
		<!-- <el-menu-item index="bannerList" @click="$router.push({ name: 'bannerList' })">
			<icon-svg name="shangpin" class="site-sidebar__menu-icon"></icon-svg>
			<span slot="title">商城配置</span>
		</el-menu-item>
		<el-menu-item index="allocationList" @click="$router.push({ name: 'allocationList' })">
			<icon-svg name="system" class="site-sidebar__menu-icon"></icon-svg>
			<span slot="title">系统配置</span>
		</el-menu-item> -->
        <sub-menu
          v-for="menu in menuList"
          :key="menu.menuId"
          :menu="menu"
          :dynamicMenuRoutes="dynamicMenuRoutes">
        </sub-menu>
      </el-menu>
    </div>
  </aside>
</template>

<script>
  import SubMenu from './main-sidebar-sub-menu'
  import { isURL } from '@/utils/validate'
  export default {
    data () {
      return {
        dynamicMenuRoutes: []
      }
    },
    components: {
      SubMenu
    },
    computed: {
      sidebarLayoutSkin: {
        get () { return this.$store.state.common.sidebarLayoutSkin }
      },
      sidebarFold: {
        get () { return this.$store.state.common.sidebarFold }
      },
      menuList: {
        get () { return this.$store.state.common.menuList },
        set (val) { this.$store.commit('common/updateMenuList', val) }
      },
      menuActiveName: {
        get () { return this.$store.state.common.menuActiveName },
        set (val) { this.$store.commit('common/updateMenuActiveName', val) }
      },
      mainTabs: {
        get () { return this.$store.state.common.mainTabs },
        set (val) { this.$store.commit('common/updateMainTabs', val) }
      },
      mainTabsActiveName: {
        get () { return this.$store.state.common.mainTabsActiveName },
        set (val) { this.$store.commit('common/updateMainTabsActiveName', val) }
      }
    },
    watch: {
      $route: 'routeHandle'
    },
    created () {
      this.menuList = JSON.parse(sessionStorage.getItem('menuList') || '[]')
      this.dynamicMenuRoutes = JSON.parse(sessionStorage.getItem('dynamicMenuRoutes') || '[]')
      this.routeHandle(this.$route)
    },
    methods: {
      // 路由操作
      routeHandle (route) {
        if (route.meta.isTab) {
          // tab选中, 不存在先添加
          var tab = this.mainTabs.filter(item => item.name === route.name)[0]
          if (!tab) {
            if (route.meta.isDynamic) {
              route = this.dynamicMenuRoutes.filter(item => item.name === route.name)[0]
              if (!route) {
                return console.error('未能找到可用标签页!')
              }
            }
            tab = {
              menuId: route.meta.menuId || route.name,
              name: route.name,
              title: route.meta.title,
              type: isURL(route.meta.iframeUrl) ? 'iframe' : 'module',
              iframeUrl: route.meta.iframeUrl || '',
              params: route.params,
              query: route.query
            }
            this.mainTabs = this.mainTabs.concat(tab)
          }
          this.menuActiveName = tab.menuId + ''
          this.mainTabsActiveName = tab.name
        }
      }
    }
  }
</script>
