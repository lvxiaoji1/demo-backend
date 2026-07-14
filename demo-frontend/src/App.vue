<template>
    <div class="app">
        <div class="nav">
            <router-link to="/users" class="nav-link">用户管理</router-link>
            <router-link to="/articles" class="nav-link">文章管理</router-link>
            <router-link to="/categories" class="nav-link">分类管理</router-link>
        </div>
        <router-view />
    </div>
</template>

<style>
* { margin: 0; padding: 0; box-sizing: border-box; }
body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif; background: #f5f5f5; color: #333; }
.app { max-width: 900px; margin: 0 auto; padding: 20px; }
.nav { display: flex; gap: 4px; margin-bottom: 24px; background: white; border-radius: 8px; padding: 8px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
.nav-link { padding: 8px 20px; text-decoration: none; color: #555; font-size: 14px; border-radius: 4px; font-weight: 500; }
.nav-link:hover { background: #f0f0f0; }
.nav-link.router-link-active { background: #409eff; color: white; }
</style>