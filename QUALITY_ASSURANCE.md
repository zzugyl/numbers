# 质量保证方案 - Baby Numbers

## 目标
建立完整的质量保证体系，确保应用稳定性、正确性和可维护性。

---

## 📊 当前状态

**完成度：0% → 目标 60%**

- ❌ 无单元测试
- ❌ 无 UI 测试
- ❌ 无集成测试
- ✅ 基础依赖已配置（JUnit, Espresso, Compose Test）
- ⚠️ 无测试目录结构（`test/`, `androidTest/`）

---

## 🎯 质量保证策略

### 分层测试金字塔

```
        /\
       /  \     UI 测试 (10%)
      /----\    - E2E 场景
     /      \   - 关键用户流程
    /--------\  集成测试 (20%)
   /          \ - ViewModel + Repository
  /------------\ 单元测试 (70%)
 /              \ - 工具函数
/________________\ - 业务逻辑
```

**优先级**：
1. **单元测试** - 最快反馈，最容易维护
2. **集成测试** - 验证模块协作
3. **UI 测试** - 验证用户交互

---

## 📋 实施计划

### Phase 1: 基础设施（1-2 天）

#### 1.1 创建测试目录结构

```
app/src/
├── main/
│   ├── java/
│   └── res/
├── test/                    # 单元测试
│   └── java/
│       └── com/babynumbers/
│           ├── util/
│           ├── audio/
│           └── viewmodel/
└── androidTest/             # 集成 & UI 测试
    └── java/
        └── com/babynumbers/
            └── ui/
```

#### 1.2 配置测试依赖

**build.gradle.kts 添加**：

```kotlin
dependencies {
    // 单元测试
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("io.mockk:mockk:1.13.10")
    testImplementation("app.cash.turbine:turbine:1.0.0")

    // 集成测试
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Compose UI 测试
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation("androidx.navigation:navigation-testing:2.7.7")

    // Hilt 测试
    testImplementation("com.google.dagger:hilt-android-testing:2.50")
    kaptTest("com.google.dagger:hilt-compiler:2.50")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.50")
    kaptAndroidTest("com.google.dagger:hilt-compiler:2.50")
}
```

#### 1.3 配置测试选项

```kotlin
android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["deployTestApk"] = "true"
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
        animationsDisabled = true
    }
}
```

---

### Phase 2: 单元测试（3-5 天）

#### 2.1 工具类测试

**优先级：⭐⭐⭐⭐⭐**

```kotlin
// app/src/test/java/com/babynumbers/util/NumberUtilsTest.kt
class NumberUtilsTest {
    @Test
    fun `getChineseNumberName 1 to 10`() {
        assertEquals("一", NumberUtils.getChineseNumberName(1))
        assertEquals("十", NumberUtils.getChineseNumberName(10))
    }

    @Test
    fun `getChineseNumberName teens`() {
        assertEquals("十一", NumberUtils.getChineseNumberName(11))
        assertEquals("二十", NumberUtils.getChineseNumberName(20))
    }

    @Test
    fun `getChineseNumberName compound`() {
        assertEquals("二十五", NumberUtils.getChineseNumberName(25))
    }
}
```

#### 2.2 ViewModel 测试

**优先级：⭐⭐⭐⭐⭐**

```kotlin
// app/src/test/java/com/babynumbers/viewmodel/LearningViewModelTest.kt
@OptIn(ExperimentalCoroutinesApi::class)
class LearningViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: LearningViewModel
    private lateinit var repository: LearningRepository

    @Before
    fun setup() {
        repository = mockk()
        viewModel = LearningViewModel(repository, audioPlayer)
    }

    @Test
    fun `setLanguage updates uiState`() = runTest {
        // Given
        coEvery { repository.setLanguage("en") } returns Unit

        // When
        viewModel.setLanguage("en")

        // Then
        assertEquals("en", viewModel.uiState.value.language)
    }

    @Test
    fun `getStageProgress calculates correctly`() {
        // Given - stage 1 (1-10), completed 1-5
        viewModel = LearningViewModel(repository, audioPlayer).apply {
            _uiState.value = _uiState.value.copy(
                completedNumbers = setOf(1, 2, 3, 4, 5)
            )
        }

        // When & Then
        assertEquals(0.5f, viewModel.getStageProgress(1))
        assertEquals(0f, viewModel.getStageProgress(2))
    }
}
```

#### 2.3 Repository 测试

**优先级：⭐⭐⭐⭐**

```kotlin
// app/src/test/java/com/babynumbers/data/repository/LearningRepositoryTest.kt
class LearningRepositoryTest {
    private lateinit var repository: LearningRepository
    private lateinit var dataStore: DataStore<Preferences>

    @Before
    fun setup() {
        dataStore = TestDataStore(
            scope = TestScope(),
            initialData = Preferences.emptyPreferences()
        )
        repository = LearningRepository(dataStore)
    }

    @Test
    fun `markNumberCompleted adds to completedNumbers`() = runTest {
        // When
        repository.markNumberCompleted(5)

        // Then
        val completed = repository.completedNumbers.first()
        assertTrue(5 in completed)
    }
}
```

---

### Phase 3: UI 测试（2-3 天）

#### 3.1 屏幕测试

```kotlin
// app/src/androidTest/java/com/babynumbers/ui/screens/HomeScreenTest.kt
@HiltAndroidTest
class HomeScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun app_launches() {
        composeTestRule.onNodeWithText("宝宝学数字").assertIsDisplayed()
    }

    @Test
    fun languageToggle_switchesLanguage() {
        // 默认中文
        composeTestRule.onNodeWithText("English").assertIsDisplayed()

        // 点击切换
        composeTestRule.onNodeWithContentDescription("Language Toggle").performClick()

        // 验证切换成功
        composeTestRule.onNodeWithText("中文").assertIsDisplayed()
    }

    @Test
    fun stageSelector_navigatesToStage() {
        // 点击第一阶段
        composeTestRule.onNodeWithText("第一阶段").performClick()

        // 验证跳转到阶段页
        composeTestRule.onNodeWithText("1").assertIsDisplayed()
    }
}
```

#### 3.2 游戏流程测试

```kotlin
// app/src/androidTest/java/com/babynumbers/ui/screens/MatchingGameTest.kt
@HiltAndroidTest
class MatchingGameTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun matchingGame_correctMatch_playsSound() {
        // 进入配对游戏
        // ...

        // 选择两张卡片
        composeTestRule.onNodeWithTag("card_0").performClick()
        composeTestRule.onNodeWithTag("card_1").performClick()

        // 验证匹配成功
        composeTestRule.onNodeWithText("✓").assertIsDisplayed()
    }
}
```

---

### Phase 4: 集成测试（1-2 天）

#### 4.1 关键用户流程

```kotlin
// app/src/androidTest/java/com/babynumbers/E2EFlowTest.kt
@HiltAndroidTest
class E2EFlowTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun learnNumber_marksComplete_progressSaved() {
        // 1. 选择第一阶段
        composeTestRule.onNodeWithText("第一阶段").performClick()

        // 2. 选择数字 1
        composeTestRule.onNodeWithText("1").performClick()

        // 3. 播放发音
        composeTestRule.onNodeWithContentDescription("播放发音").performClick()

        // 4. 标记完成
        composeTestRule.onNodeWithContentDescription("完成").performClick()

        // 5. 返回主页
        composeTestRule.onNodeWithContentDescription("返回").performClick()

        // 6. 验证进度更新
        composeTestRule.onNodeWithText("10%").assertIsDisplayed()
    }

    @Test
    fun playMatchingGame_completesGame_returnsHome() {
        // 1. 主页 → 游戏中心
        composeTestRule.onNodeWithContentDescription("游戏中心").performClick()

        // 2. 配对游戏
        composeTestRule.onNodeWithText("配对游戏").performClick()

        // 3. 完成游戏
        // ...

        // 4. 验证返回游戏中心
        composeTestRule.onNodeWithText("游戏中心").assertIsDisplayed()
    }
}
```

---

### Phase 5: 自动化 & CI（1-2 天）

#### 5.1 测试脚本

```bash
#!/bin/bash
# scripts/run_tests.sh

echo "🧪 Running Unit Tests..."
./gradlew test

echo "🎨 Running UI Tests..."
./gradlew connectedAndroidTest

echo "📊 Generating Coverage Report..."
./gradlew jacocoTestReport

echo "✅ All Tests Passed!"
```

#### 5.2 GitHub Actions / GitLab CI

```yaml
# .github/workflows/test.yml
name: Tests

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Run Unit Tests
        run: ./gradlew test

      - name: Run UI Tests
        uses: reactivecircus/android-emulator-runner@v2
        with:
          api-level: 34
          script: ./gradlew connectedAndroidTest

      - name: Upload Coverage
        uses: codecov/codecov-action@v3
```

---

## 🛠️ 测试工具链

### 必需工具

| 工具 | 用途 | 优先级 |
|------|------|--------|
| **JUnit 4** | 单元测试框架 | ⭐⭐⭐⭐⭐ |
| **MockK** | Mock 框架（Kotlin 友好） | ⭐⭐⭐⭐⭐ |
| **Turbine** | Flow 测试 | ⭐⭐⭐⭐ |
| **Compose Test** | UI 测试 | ⭐⭐⭐⭐ |
| **Hilt Testing** | 依赖注入测试 | ⭐⭐⭐⭐ |
| **Jacoco** | 代码覆盖率 | ⭐⭐⭐ |

### 可选工具

| 工具 | 用途 |
|------|------|
| **Detekt** | 静态代码分析 |
| **Ktlint** | Kotlin 代码规范 |
| **Danger** | PR 自动化检查 |
| **Firebase Test Lab** | 云端设备测试 |

---

## 📈 代码覆盖率目标

```
总体覆盖率    → 80%
ViewModel     → 90%
Repository    → 85%
Util          → 95%
UI            → 60%
```

**检查覆盖率**：
```bash
./gradlew jacocoTestReport
# 报告生成：app/build/reports/jacoco/jacocoTestReport/html/index.html
```

---

## 🎯 测试策略

### 什么需要测试？

✅ **必须测试**
- 业务逻辑（ViewModel, Repository）
- 工具函数（NumberUtils, DateUtils）
- 关键用户流程
- 状态管理（DataStore, Flow）

❌ **不需要测试**
- Android 框架代码
- Compose 组件（除非有复杂逻辑）
- 数据类（data class）

### 测试原则

1. **AAA 模式**：Arrange → Act → Assert
2. **单一职责**：每个测试只验证一个场景
3. **可读性**：测试名即文档
4. **独立性**：测试间互不依赖
5. **快速执行**：单元测试 < 100ms

---

## 🚀 快速开始

### 步骤 1: 创建目录结构

```bash
mkdir -p app/src/test/java/com/babynumbers/{util,viewmodel,data}
mkdir -p app/src/androidTest/java/com/babynumbers/ui
```

### 步骤 2: 添加测试依赖

参考 **Phase 1.2** 更新 `build.gradle.kts`

### 步骤 3: 编写第一个测试

```kotlin
// app/src/test/java/com/babynumbers/util/NumberUtilsTest.kt
class NumberUtilsTest {
    @Test
    fun `test example`() {
        assertEquals(4, 2 + 2)
    }
}
```

### 步骤 4: 运行测试

```bash
./gradlew test
```

---

## 📚 学习资源

- [Android Testing Guide](https://developer.android.com/training/testing)
- [Compose Testing](https://developer.android.com/jetpack/compose/testing)
- [MockK Documentation](https://mockk.io/)
- [Turbine for Flow](https://github.com/cashapp/turbine)

---

## ❓ 常见问题

**Q: 应该先写测试还是先写代码？**
A: 建议先写核心逻辑的单元测试，再实现功能。

**Q: 测试覆盖率多少合适？**
A: 业务逻辑 80-90%，UI 60%+ 即可。

**Q: Mock 太多会不会降低测试价值？**
A: 只 Mock 外部依赖，内部逻辑尽量真实测试。
