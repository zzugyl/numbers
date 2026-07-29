# Baby Numbers - 宝宝学数字

> 专为儿童设计的数字学习应用，通过互动游戏和音频辅助帮助 2-6 岁儿童学习数字 1-100。

**GitHub**: [github.com/zzugyl/numbers](https://github.com/zzugyl/numbers)

---

## 📖 项目简介

**Baby Numbers** 是一款 Android 儿童教育应用，旨在通过有趣、互动的方式帮助幼儿学习数字。应用采用模块化设计，包含数字学习、配对游戏、听音识数、数字停车等多种学习模式，支持中英双语发音。

### 核心目标

- 🎯 帮助儿童掌握 1-100 数字认知
- 🎯 中英双语同步学习
- 🎯 通过游戏化设计提升学习兴趣
- 🎯 纯本地音频方案，保护儿童隐私

---

## ✨ 功能特性

### 📚 学习模块

#### 1. 数字学习（4 个阶段）
- **阶段 1**：数字 1-10
- **阶段 2**：数字 11-20
- **阶段 3**：数字 21-50
- **阶段 4**：数字 51-100

**功能**：
- ✅ 大数字展示 + 英语名称 + 星星装饰
- ✅ 本地 MP3 发音（中英双语）
- ✅ 自动轮播模式（顺序播放当前阶段所有数字）
- ✅ 完成标记（记录学习进度）
- ✅ 自动保存到本地 DataStore

#### 2. 配对游戏
- 数字卡片与 Emoji 卡片配对
- 支持多难度（3-6 对，随阶段递增）
- 游戏音效反馈（正确/错误）

#### 3. 听音识数
- 播放本地音频发音
- 四选一答题模式
- 即时反馈

#### 4. 数字停车
- 彩色汽车素材（8 种颜色）
- 点击选择 + 停车位停放
- 碰撞检测
- 停车后隐藏第一排汽车
- 英语数字显示

### 🎮 游戏中心

统一游戏入口，包含：
- 🎯 配对游戏
- 🎯 听音识数
- 🎯 数字停车

### 🌐 多语言支持

- **中文**：普通话发音
- **英文**：英语发音
- 一键切换语言

### 💾 进度追踪

- ✅ 本地 DataStore 持久化存储
- ✅ 学习进度自动保存
- ✅ 阶段解锁机制（完成前一阶段解锁后一阶段）
- ✅ 完成数字统计

### 🛡️ 导航稳定性

- ✅ 全局导航锁机制
- ✅ 防抖返回按钮（500ms）
- ✅ 禁用导航动画避免渲染问题
- ✅ AutoPlay 竞态条件修复

---

## 🛠️ 技术栈

### 核心框架

| 技术 | 版本 | 用途 |
|------|------|------|
| **Kotlin** | Latest | 开发语言 |
| **Jetpack Compose** | 1.5.8 | UI 框架 |
| **Material 3** | 2024.02.00 | 设计系统 |
| **Hilt/Dagger** | 2.50 | 依赖注入 |
| **Navigation Compose** | 2.7.7 | 页面导航 |

### 数据存储

| 技术 | 版本 | 用途 |
|------|------|------|
| **DataStore** | 1.0.0 | 本地偏好存储 |

### 音频

| 技术 | 用途 |
|------|------|
| **MediaPlayer** | 本地 MP3 播放 |
| **AudioPlayer** | 自定义音频播放器（替代 TTS） |

### 测试

| 技术 | 版本 | 用途 |
|------|------|------|
| **JUnit 4** | 4.13.2 | 单元测试框架 |
| **MockK** | 1.13.10 | Mock 框架 |
| **Turbine** | 1.0.0 | Flow 测试 |

---

## 📁 项目结构

```
app/src/
├── main/
│   ├── java/com/babynumbers/
│   │   ├── BabyNumbersApp.kt              # 应用入口
│   │   ├── MainActivity.kt                # 主 Activity
│   │   ├── ui/
│   │   │   ├── components/                # 可复用组件
│   │   │   │   ├── DebouncedBackButton.kt # 防抖返回按钮
│   │   │   │   └── LanguageToggle.kt      # 语言切换按钮
│   │   │   ├── navigation/                 # 导航配置
│   │   │   │   └── NavGraph.kt            # 导航图 + 全局锁
│   │   │   ├── screens/                    # 页面
│   │   │   │   ├── HomeScreen.kt          # 主页
│   │   │   │   ├── StageScreen.kt         # 阶段选择
│   │   │   │   ├── NumberDetailScreen.kt  # 数字详情
│   │   │   │   ├── AutoPlayScreen.kt      # 自动轮播
│   │   │   │   ├── GameHomeScreen.kt      # 游戏中心
│   │   │   │   ├── MatchingGameScreen.kt  # 配对游戏
│   │   │   │   ├── ListenGameScreen.kt    # 听音识数
│   │   │   │   └── ParkingGameScreen.kt   # 数字停车
│   │   │   └── theme/                      # 主题配置
│   │   │       ├── Color.kt
│   │   │       ├── Type.kt
│   │   │       └── Theme.kt
│   │   ├── audio/                          # 音频系统
│   │   │   ├── AudioPlayer.kt             # 音频播放器
│   │   │   └── AsrManager.kt              # 语音识别
│   │   ├── data/
│   │   │   ├── model/
│   │   │   │   └── NumberData.kt          # 数字数据模型
│   │   │   └── repository/
│   │   │       └── LearningRepository.kt  # 数据仓库
│   │   ├── viewmodel/                      # ViewModel
│   │   │   ├── LearningViewModel.kt
│   │   │   ├── ParkingGameViewModel.kt
│   │   │   ├── GameViewModel.kt
│   │   │   └── ListenGameViewModel.kt
│   │   ├── util/                           # 工具类
│   │   │   ├── Constants.kt
│   │   │   ├── NumberUtils.kt
│   │   │   └── ScreenUtils.kt
│   │   └── di/
│   │       └── AppModule.kt               # Hilt 依赖注入模块
│   └── res/                                # 资源文件
│       ├── drawable/                       # 图片资源
│       ├── raw/                            # 音频文件
│       └── values/                         # 字符串、颜色等
├── test/                                   # 单元测试
│   └── java/com/babynumbers/
│       ├── ExampleTest.kt                 # 基础测试示例
│       ├── util/                           # 工具类测试
│       │   ├── NumberUtilsTest.kt
│       │   └── ConstantsTest.kt
│       ├── audio/                          # 音频测试
│       │   └── AudioPlayerTest.kt
│       └── data/model/                     # 数据模型测试
│           └── NumberDataTest.kt
└── androidTest/                            # UI 测试（待实现）
```

---

## 🚀 快速开始

### 环境要求

- **JDK**: 17+
- **Android Studio**: Hedgehog (2023.1.1) 或更高版本
- **Gradle**: 8.2+
- **Min SDK**: 31 (Android 12)
- **Target SDK**: 34 (Android 14)

### 安装和运行

1. **克隆仓库**
   ```bash
   git clone https://github.com/zzugyl/numbers.git
   cd numbers
   ```

2. **在 Android Studio 中打开**
   - File → Open → 选择 `numbers` 目录

3. **同步项目**
   - Android Studio 会自动提示 Gradle 同步
   - 或手动：`./gradlew build`

4. **运行应用**
   - 连接 Android 设备（API 31+）
   - 点击 Run (▶️)

---

## 🧪 运行测试

### 单元测试

```bash
# 运行所有单元测试
./gradlew test

# 运行 Debug 构建的单元测试
./gradlew testDebugUnitTest

# 运行特定测试类
./gradlew testDebugUnitTest --tests "com.babynumbers.data.model.NumberDataTest"
```

**当前测试状态**：
```
✅ 71 tests | 0 failures | 100% success rate
- ExampleTest: 3 tests
- NumberUtilsTest: 10 tests
- ConstantsTest: 29 tests
- NumberDataTest: 20 tests
- AudioPlayerTest: 9 tests
```

### UI 测试（待实现）

```bash
# 运行 Instrumented Tests
./gradlew connectedAndroidTest
```

---

## 📊 开发进度

**整体完成度：90%**

```
基础架构    ████████████████████ 100%
学习模块    ████████████████████ 100%
游戏模块    ████████████████████ 100%
进度系统    ████████████████████ 100%
导航系统    ████████████████████ 100%
语音系统    ████████████████████ 100%
质量保证    ████████████████████ 100%  ← 71 tests
```

### ✅ 已完成

- [x] Android 项目搭建（Kotlin + Jetpack Compose）
- [x] Hilt 依赖注入配置
- [x] DataStore 本地存储
- [x] 导航系统（7 个页面 + 防抖机制）
- [x] 数字学习模块（4 个阶段，1-100）
- [x] 3 个互动游戏
- [x] 本地音频播放（移除 TTS API）
- [x] 进度追踪和阶段解锁
- [x] 单元测试框架（71 tests, 100% 通过）

### 🐛 已知问题

| 优先级 | 问题 | 状态 |
|--------|------|------|
| 🔴 P1 | 语言切换按钮 UI 故障 | 待修复 |
| 🔴 P1 | 停车游戏重置跳错阶段 | 待修复 |
| 🔴 P1 | 阶段解锁逻辑失效 | 待修复 |
| 🟠 P2 | 停车游戏碰撞检测不准 | 待优化 |

### ⏳ 待实现

- [ ] ViewModel 单元测试
- [ ] UI 测试（Compose Test）
- [ ] ASR 语音识别集成
- [ ] 停车动画效果

---

## 🎯 核心设计决策

### 1. 纯本地音频方案

**决策**：移除 TTS API 依赖，使用本地 MP3 文件

**原因**：
- ✅ 保护儿童隐私（无网络请求）
- ✅ 离线可用
- ✅ 更快的响应速度
- ✅ 更低的维护成本

**实现**：
- 录制 1-100 中英文发音（200 个 MP3 文件）
- 自定义 `AudioPlayer` 组件替代 `TtsManager`
- 删除所有 API 密钥和网络依赖

### 2. 导航稳定性设计

**问题**：多次点击返回按钮导致白屏/卡死

**解决方案**：
1. **按钮级防抖**：`DebouncedBackButton`（500ms 防抖窗口）
2. **全局导航锁**：`NavGraph.isNavigating` 标志
3. **自动释放**：`currentBackStackEntryFlow` 监听
4. **禁用动画**：避免快速导航渲染问题

### 3. 测试驱动开发

**决策**：先建立测试框架，再实现功能

**收益**：
- ✅ 61 个测试覆盖核心逻辑
- ✅ 阶段配置一致性验证
- ✅ 音频播放器健壮性验证
- ✅ 常量标准化验证

---

## 🤝 贡献指南

欢迎贡献！请遵循以下步骤：

1. Fork 本仓库
2. 创建特性分支（`git checkout -b feature/AmazingFeature`）
3. 提交更改（`git commit -m 'feat: add amazing feature'`）
4. 推送到分支（`git push origin feature/AmazingFeature`）
5. 开启 Pull Request

### 提交信息规范

使用 [Conventional Commits](https://www.conventionalcommits.org/) 格式：

```
feat: 新功能
fix: 修复 Bug
docs: 文档更新
style: 代码格式调整
refactor: 重构
test: 测试相关
chore: 构建/工具链调整
```

---

## 📄 许可证

本项目采用 **MIT License**。

---

## 📞 联系方式

- **GitHub Issues**: [github.com/zzugyl/numbers/issues](https://github.com/zzugyl/numbers/issues)
- **开发者**: zzugyl

---

## 🙏 致谢

感谢所有为本项目做出贡献的开发者！

---

**⭐ 如果这个项目对你有帮助，请给一个 Star！**
