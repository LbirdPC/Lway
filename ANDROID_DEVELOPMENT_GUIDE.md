# Android 应用开发入门指南

## 一、开发流程概览

### 1. 整体流程

```
需求分析 → 环境搭建 → 项目创建 → 功能开发 → 测试调试 → 打包发布
     ↓           ↓           ↓          ↓          ↓          ↓
  明确目标    安装工具    初始化    编写代码    真机测试    上架商店
```

### 2. 详细步骤

| 阶段 | 内容 | 关键产出 |
|------|------|----------|
| **需求分析** | 明确功能需求、目标用户、设计风格 | 需求文档、原型设计 |
| **环境搭建** | 安装 Android Studio、配置 SDK | 开发环境就绪 |
| **项目创建** | 选择模板、配置项目结构 | 项目框架 |
| **UI 开发** | 使用 Compose/XML 创建界面 | 界面代码 |
| **业务逻辑** | 实现功能、数据处理 | 业务代码 |
| **测试调试** | 单元测试、真机测试 | Bug 修复 |
| **打包发布** | 生成 APK、签名、上架 | 发布版本 |

---

## 二、常用开发工具

### 1. 核心工具

#### Android Studio
- **官方 IDE**，集代码编辑、编译、调试、模拟器于一体
- **下载地址**：https://developer.android.com/studio
- **主要功能**：
  - 智能代码补全
  - 可视化布局编辑器
  - 真机/模拟器调试
  - Gradle 构建管理

#### SDK Tools
- **Android SDK**：包含各种 API 版本
- **Build Tools**：编译构建工具
- **Emulator**：虚拟设备模拟器

### 2. 辅助工具

| 工具 | 用途 |
|------|------|
| **Git** | 版本控制 |
| **Firebase** | 后端服务（认证、数据库、存储） |
| **Postman** | API 测试 |
| **Figma** | UI 设计 |

---

## 三、核心技术与框架

### 1. 编程语言

#### Kotlin（推荐）
- 现代编程语言，官方推荐用于 Android 开发
- 简洁、安全、与 Java 完全兼容
- 支持空安全、协程等现代特性

#### Java
- 传统语言，仍有大量项目使用

### 2. UI 框架

#### Jetpack Compose（现代）
- 声明式 UI 框架，类似 React/Vue
- 代码即界面，无需 XML
- 更简洁、更高效

#### View System（传统）
- 使用 XML 定义布局
- 通过 Activity/Fragment 管理界面

### 3. 架构模式

#### MVVM（Model-View-ViewModel）
- **Model**：数据模型和业务逻辑
- **View**：界面展示
- **ViewModel**：管理 UI 状态

#### Clean Architecture
- 将代码分层：UI → Domain → Data
- 提高代码可测试性和可维护性

### 4. 常用库

| 库 | 用途 |
|----|------|
| **Room** | 本地数据库（SQLite 封装） |
| **Hilt** | 依赖注入 |
| **Retrofit** | 网络请求 |
| **Navigation** | 页面导航 |
| **Coil** | 图片加载 |
| **Coroutines** | 异步编程 |

---

## 四、项目结构规范

### 标准项目结构

```
app/
├── src/main/
│   ├── java/com/example/app/
│   │   ├── data/          # 数据层（数据库、网络）
│   │   │   ├── local/     # 本地数据
│   │   │   ├── remote/    # 远程数据
│   │   │   └── repository/ # 仓库层
│   │   ├── domain/        # 领域层（业务逻辑）
│   │   │   ├── model/     # 数据模型
│   │   │   ├── repository/ # 仓库接口
│   │   │   └── usecase/   # 用例
│   │   ├── presentation/  # 表现层（UI）
│   │   │   ├── ui/        # Compose 界面
│   │   │   ├── viewmodel/ # ViewModel
│   │   │   └── navigation/ # 导航
│   │   ├── di/            # 依赖注入配置
│   │   └── Application.kt # 应用入口
│   └── res/               # 资源文件
│       ├── drawable/      # 图片资源
│       ├── layout/        # XML 布局（传统）
│       ├── values/        # 字符串、颜色、样式
│       └── mipmap/        # 应用图标
└── build.gradle.kts       # 模块配置
```

---

## 五、Hello World 示例

### 1. 创建新项目

1. 打开 Android Studio → New Project
2. 选择 "Empty Activity" → Next
3. 填写应用名称、包名 → Finish

### 2. 修改布局（XML 方式）

`res/layout/activity_main.xml`:
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center"
    android:orientation="vertical">

    <TextView
        android:id="@+id/helloText"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Hello World!"
        android:textSize="24sp"/>

    <Button
        android:id="@+id/clickButton"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="点击我"/>

</LinearLayout>
```

### 3. 修改 Activity

`MainActivity.kt`:
```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.helloText)
        val button = findViewById<Button>(R.id.clickButton)

        button.setOnClickListener {
            textView.text = "你点击了按钮！"
        }
    }
}
```

### 4. 运行应用

1. 连接手机或启动模拟器
2. 点击 ▶ 按钮运行

---

## 六、调试技巧

### 1. Log 日志

```kotlin
Log.d("MainActivity", "调试信息")  // Debug
Log.e("MainActivity", "错误信息")  // Error
Log.i("MainActivity", "信息")      // Info
```

### 2. 断点调试

1. 点击代码行号旁边设置断点（红点）
2. 点击 "Debug" 按钮运行
3. 程序会在断点处暂停，可查看变量值

### 3. 真机调试

1. 手机开启开发者模式
2. 开启 USB 调试
3. 连接数据线，选择 "PTP" 模式

---

## 七、打包发布

### 1. 生成签名 APK

1. Build → Generate Signed Bundle / APK
2. 选择 APK → Next
3. 创建/选择密钥库（.jks 文件）
4. 设置构建类型为 Release
5. 生成 APK 文件

### 2. 上架应用商店

| 商店 | 平台 | 费用 |
|------|------|------|
| 华为应用市场 | https://developer.huawei.com | 免费 |
| 小米应用商店 | https://dev.mi.com | 免费 |
| OPPO 软件商店 | https://open.oppomobile.com | 免费 |
| vivo 开放平台 | https://dev.vivo.com.cn | 免费 |
| 应用宝 | https://app.open.qq.com | 免费 |
| Google Play | https://play.google.com/console | $25 |

---

## 八、学习资源推荐

### 官方文档
- [Android 开发者官网](https://developer.android.com)
- [Jetpack Compose 教程](https://developer.android.com/jetpack/compose/tutorial)

### 学习路线
1. **基础**：Kotlin 语法 → Android 基础组件
2. **进阶**：MVVM 架构 → Jetpack 组件
3. **实战**：完整项目开发 → 发布上架

### 社区资源
- Stack Overflow
- GitHub 开源项目
- Medium 技术文章

---

## 九、总结

| 要点 | 说明 |
|------|------|
| **开发工具** | Android Studio + SDK |
| **语言** | Kotlin 为主，Java 兼容 |
| **UI** | Jetpack Compose（推荐）或 XML |
| **架构** | MVVM + Clean Architecture |
| **流程** | 需求 → 开发 → 测试 → 发布 |

**关键建议**：
1. 从简单项目开始，逐步积累
2. 多写代码，多调试
3. 阅读优秀开源项目代码
4. 关注官方文档更新
