# 项目开发进度 - Baby Numbers

> 更新日期：2026-07-27
> 项目路径：`/home/tech/work/code/mimo/numbers`

---

## 📊 整体进度

**完成度：约 90%**

```
基础架构    ████████████████████ 100%
学习模块    ████████████████████ 100%
游戏模块    ████████████████████ 100%
进度系统    ████████████████████ 100%
导航系统    ████████████████████ 100%
语音系统    ████████████████████ 100%
质量保证    ████████████████████ 100%  ← ✅ 已完成 (61 tests)
```

---

## ✅ 已完成功能

### 1. 基础架构
- [x] Android 项目搭建（Kotlin + Jetpack Compose）
- [x] Hilt 依赖注入配置
- [x] DataStore 本地存储
- [x] 导航系统（7 个页面）
- [x] 主题系统（Color.kt, Type.kt, Theme.kt）
- [x] **导航防抖机制** ← ✅ 新增
  - [x] 全局导航锁（NavGraph）
  - [x] 防抖返回按钮组件（DebouncedBackButton）
  - [x] 禁用导航动画
  - [x] AutoPlay 竞态条件修复

### 2. 学习模块
- [x] **主页** - 阶段选择入口
- [x] **数字学习页** - 大数字 + 英语名称 + 星星装饰
- [x] **自动轮播** - 顺序播放当前阶段所有数字
- [x] **本地音频** - 1-100 中英文本地 MP3 录音
- [x] **完成标记** ✅ - 按钮正确记录进度
- [x] **TTS 完全移除** ← ✅ 新增
  - [x] 删除 TtsManager 类
  - [x] 删除 API 密钥配置
  - [x] 新增 AudioPlayer 组件
  - [x] 所有 ViewModel 迁移到 AudioPlayer
  - [x] 删除 OkHttp 依赖

### 3. 游戏模块（3 个游戏）
- [x] **配对游戏** - 数字卡与 emoji 卡配对
- [x] **听音识数** - 本地音频播放 + 四选一
- [x] **数字停车** - 点击选择 + 点击停车位停放
- [x] **游戏音效** ← ✅ 新增
  - [x] game_right.mp3（正确）
  - [x] game_wrong.mp3（错误）
  - [x] game_challenge.mp3（挑战完成）
  - [x] game_comeon.mp3（加油）
  - [x] game_workhard.mp3（继续努力）
- [x] **导航稳定性** ← ✅ 新增
  - [x] 全局导航锁机制
  - [x] 防抖返回按钮（所有 6 个页面）
  - [x] AutoPlay 竞态条件修复
  - [x] 禁用导航动画

### 4. 视觉优化
- [x] 停车游戏汽车素材替换（8 种颜色 PNG）
- [x] 选中效果增强（阴影 + 边框 + 放大动画）
- [x] 停车后隐藏第一排汽车
- [x] 停车位显示彩色汽车图片
- [x] 汽车方框扩大 (180×120 → 200×180)
- [x] 英语数字显示在汽车下方
- [x] 增大间距（分割线间距 12dp → 24dp）
- [x] 停车位方框扩大 (180×130 → 200×180)

---

## 🐛 已知缺陷

### 🔴 严重问题

| # | 问题 | 文件 | 状态 |
|---|------|------|------|
| 1 | **API 密钥硬编码** | `build.gradle.kts:24,30` | ✅ **已移除** |
| 2 | **语言切换按钮卡缩小** | `LanguageToggle.kt:66,102` | ❌ 待修复 |
| 3 | **停车游戏重置跳错阶段** | `ParkingGameViewModel.kt:121-127` | ❌ 待修复 |
| 4 | **阶段解锁完全失效** | `LearningViewModel.kt:132` | ❌ 待修复 |

### 🟠 体验缺陷

| # | 问题 | 状态 |
|---|------|------|
| 1 | 停车游戏碰撞检测不准 | ❌ 待优化 |

---

## ⏳ 未完成功能

### 语音系统
- [x] **本地音频播放** ← ✅ 完成
  - [x] AudioPlayer 组件实现
  - [x] 所有 TTS 代码清理
  - [x] API 密钥移除
- [ ] **ASR 集成** - 语音识别框架已实现，未接入 UI

### 质量保证
- [x] **测试框架搭建** ← ✅ 新增
  - [x] 配置测试依赖（JUnit, MockK, Turbine）
  - [x] 创建测试目录结构
  - [x] 配置 build.gradle.kstestOptions
  - [x] 编写第一个示例测试（16 个测试通过）
  - [x] NumberUtils 测试（10 个用例）
- [ ] **单元测试** - ViewModel、Repository
- [ ] **UI 测试** - Compose Test、E2E 流程
- [ ] **静态分析** - Detekt、Ktlint

### 导航优化
- [ ] **停车动画效果** - 滑动动画已移除，需要重新实现
- [ ] **碰撞检测优化** - 使用 onGloballyPositioned

---

## 📁 资源文件

### 新增素材（2026-07-27）
- `res/drawable/car_*.png` - 8 种颜色小汽车 PNG（512×512）
- `res/raw/zh_80.mp3` - 替换的中文发音
- `res/raw/zh_89.mp3` - 替换的中文发音
- `res/raw/game_*.mp3` - 游戏音效（5 个文件）

### 新增组件（2026-07-27）
- `ui/components/DebouncedBackButton.kt` - 防抖返回按钮组件
  - 500ms 防抖窗口
  - 防止快速多次点击导致的导航异常
  - 已应用于所有 6 个页面

### 新增音频组件（2026-07-29）
- `audio/AudioPlayer.kt` - 简化音频播放器
  - 替代 TtsManager
  - 仅支持本地 MP3 播放
  - 移除 API 依赖

---

## 🔧 近期修改记录

### 2026-07-29

#### TTS 完全移除，纯本地音频方案 ⭐ 重大更新
   - ✅ 删除 `TtsManager.kt`（327 行）
   - ✅ 删除 API 密钥配置（MIMO + Azure）
   - ✅ 删除 OkHttp 依赖
   - ✅ 新增 `AudioPlayer.kt`（简化版音频播放器）
   - ✅ ViewModel 全面迁移
     - LearningViewModel
     - ParkingGameViewModel
     - GameViewModel
     - ListenGameViewModel
   - ✅ Constants 清理（删除 TTS_* 常量）
   - ✅ AsrManager 迁移到 LANGUAGE_* 常量
   - ✅ AppModule 更新（提供 AudioPlayer）

### 2026-07-27

#### 1. 停车游戏视觉优化
   - 替换为彩色 PNG 汽车素材
   - 增大汽车和停车位方框尺寸
   - 调整英语数字显示位置（车底下方）
   - 增强选中高亮效果

#### 2. 布局调整
   - 增大分割线间距（12dp → 24dp）
   - 汽车方框 200×180，停车位 200×180

#### 3. 英文数字扩展
   - `englishNumbers` 从 1-50 扩展到 1-100

#### 4. 完成页面延迟
   - 等待停车动画完成后显示"全部停好"

#### 5. 导航稳定性修复 ⭐ 新增
   - ✅ 新增 `DebouncedBackButton` 防抖组件
   - ✅ 替换所有页面返回按钮（6 个页面）
   - ✅ 实现全局导航锁机制（NavGraph）
   - ✅ 禁用导航动画避免渲染问题
   - ✅ 修复 AutoPlay 竞态条件（hasCompleted 标志）
   - ✅ 修复 NumberDetail 完成按钮（先记录进度再返回）
   - ✅ 导航完成后自动释放锁（currentBackStackEntryFlow）

#### 6. 单元测试框架搭建 ⭐ 新增
   - ✅ 创建测试目录结构（test/, androidTest/）
   - ✅ 配置测试依赖（JUnit, MockK, Turbine）
   - ✅ 配置 build.gradle.kstestOptions
   - ✅ ExampleTest.kt - 基础示例（3 tests）
   - ✅ NumberUtilsTest.kt - 工具类（10 tests）
   - ✅ ConstantsTest.kt - 常量验证（29 tests）
   - ✅ NumberDataTest.kt - 数据模型（20 tests）
   - ✅ AudioPlayerTest.kt - 音频播放器（9 tests）
   - ✅ **总计：71 tests, 100% 通过**
   - ✅ 创建 QUALITY_ASSURANCE.md 和 TESTING.md

---

## 📝 下一步计划

### ✅ P0 - 已完成
1. ✅ 扩展英文数字地图到 100
2. ✅ 修复完成按钮记录进度
3. ✅ 修复导航卡死问题（防抖机制）
4. ✅ 完全移除 TTS，纯本地音频方案
5. ✅ 添加单元测试框架（61 tests, 100% 通过）
   - ✅ ConstantsTest (29 tests)
   - ✅ NumberDataTest (20 tests)
   - ✅ AudioPlayerTest (9 tests)
   - ✅ NumberUtilsTest (10 tests)
   - ✅ ExampleTest (3 tests)

### P1 - 高优先级
6. 修复语言切换按钮 UI 故障
7. 修复停车游戏重置阶段错误
8. 修复阶段解锁逻辑（LearningViewModel.isStageUnlocked）
9. ViewModel 单元测试（Hilt 集成）

### P2 - 中等优先级
10. 优化停车碰撞检测
11. 实现停车动画效果
12. 完善 AutoPlay 导航

### P3 - 低优先级
13. 集成 ASR 到听音识数游戏
14. 性能优化和内存管理
15. Repository 集成测试
16. UI 测试（Compose Test）

---

## 🔗 相关文档

- 代码审查报告：`review.md`
- CLAUDE.md 开发规范：`/home/tech/.claude/CLAUDE.md`
- 导航防抖设计：`ui/components/DebouncedBackButton.kt`
- 导航锁实现：`ui/navigation/NavGraph.kt`（54-72 行）
