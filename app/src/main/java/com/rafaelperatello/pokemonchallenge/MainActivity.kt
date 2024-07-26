package com.rafaelperatello.pokemonchallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.Keep
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.coroutineScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rafaelperatello.pokemonchallenge.ui.screen.home.HomeScreen
import com.rafaelperatello.pokemonchallenge.ui.screen.settings.SettingsScreen
import com.rafaelperatello.pokemonchallenge.ui.theme.PokemonChallengeTheme
import com.rafaelperatello.pokemonchallenge.ui.widget.AppBarAction
import com.rafaelperatello.pokemonchallenge.ui.widget.PokemonAppBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var keepSplash = true
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            keepSplash
        }
        lifecycle.coroutineScope.launch {
            delay(1000)
            keepSplash = false
        }

        enableEdgeToEdge()

        setContent {
            PokemonChallengeTheme {
                val navController = rememberNavController()
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute: State<MainRoutes> =
                    remember {
                        derivedStateOf {
                            currentBackStackEntry?.destination?.route?.let { route ->
                                when (route) {
                                    MainRoutes.Home::class.qualifiedName -> MainRoutes.Home
                                    MainRoutes.Favorites::class.qualifiedName -> MainRoutes.Favorites
                                    MainRoutes.Settings::class.qualifiedName -> MainRoutes.Settings
                                    else -> null
                                }
                            } ?: MainRoutes.Home
                        }
                    }

                val appBarMenuActions: MutableState<AppBarAction?> =
                    remember {
                        mutableStateOf(null)
                    }
                val onUpdateAppBarAction: (AppBarAction?) -> Unit = { action ->
                    appBarMenuActions.value = action
                }

                val snackbarHostState = remember { SnackbarHostState() }

                PokemonScaffold(
                    navController = navController,
                    currentRoute = currentRoute,
                    appBarMenuActions = appBarMenuActions,
                    snackbarHostState = snackbarHostState,
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                    ) {
                        MainNavHost(
                            navController = navController,
                            startDestination = MainRoutes.Home,
                            currentRoute = currentRoute,
                            snackbarHostState = snackbarHostState,
                            onUpdateAppBarAction = onUpdateAppBarAction,
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun PokemonScaffold(
    navController: NavHostController = rememberNavController(),
    currentRoute: State<MainRoutes> = remember { mutableStateOf(MainRoutes.Home) },
    appBarMenuActions: State<AppBarAction?> = remember { mutableStateOf(null) },
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    body: @Composable (PaddingValues) -> Unit = {},
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { MainAppBar(appBarMenuActions) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            MainBottomBar(
                currentRoute = currentRoute,
                onItemClick = { route: MainRoutes ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        },
    ) { innerPadding ->
        body(innerPadding)
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun MainAppBar(appBarMenuActions: State<AppBarAction?>) {
    val isDarkTheme = isSystemInDarkTheme()
    val colors =
        if (isDarkTheme) {
            TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        } else {
            TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.tertiaryContainer,
                actionIconContentColor = MaterialTheme.colorScheme.tertiaryContainer,
            )
        }

    PokemonAppBar(
        title = stringResource(id = R.string.title_main),
        action = appBarMenuActions,
        colors = colors,
    )
}

@Composable
internal fun MainNavHost(
    navController: NavHostController,
    startDestination: MainRoutes,
    currentRoute: State<MainRoutes>,
    snackbarHostState: SnackbarHostState,
    onUpdateAppBarAction: (AppBarAction?) -> Unit = {},
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable<MainRoutes.Home> {
            HomeScreen(
                currentRoute = currentRoute,
                onUpdateAppBarAction = onUpdateAppBarAction,
            )
        }
        composable<MainRoutes.Favorites> {
            LaunchedEffect(currentRoute.value) {
                if (currentRoute.value == MainRoutes.Favorites) {
                    onUpdateAppBarAction(null)
                }
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = "Favorites")
            }
        }
        composable<MainRoutes.Settings> {
            LaunchedEffect(currentRoute.value) {
                if (currentRoute.value == MainRoutes.Favorites) {
                    onUpdateAppBarAction(null)
                }
            }

            SettingsScreen(snackbarHostState)
        }
    }
}

@Composable
private fun MainBottomBar(
    currentRoute: State<MainRoutes>,
    onItemClick: (MainRoutes) -> Unit = {},
) {
    // Todo make it smaller
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier
            .windowInsetsPadding(
                WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal),
            )
            .height(100.dp),
    ) {
        mainScreens.forEach { destination ->
            val selected = currentRoute.value == destination.route

            NavigationBarItem(
                modifier = Modifier.defaultMinSize(minWidth = 56.dp),
                selected = selected,
                onClick = { onItemClick(destination.route) },
                colors =
                NavigationBarItemDefaults.colors().copy(
                    selectedIndicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
                icon = {
                    val icon =
                        if (selected) {
                            destination.selectedIcon
                        } else {
                            destination.unselectedIcon
                        }

                    Icon(
                        imageVector = icon,
                        modifier = Modifier.size(24.dp),
                        contentDescription = null,
                    )
                },
            )
        }
    }
}

private val mainScreens =
    listOf(
        BottomBarItems.Home,
        BottomBarItems.Favorites,
        BottomBarItems.Settings,
    )

@Keep
@Serializable
sealed class MainRoutes {
    @Serializable
    data object Home : MainRoutes()

    @Serializable
    data object Favorites : MainRoutes()

    @Serializable
    data object Settings : MainRoutes()
}

sealed class BottomBarItems(
    val route: MainRoutes,
    val labelRes: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    data object Home : BottomBarItems(
        route = MainRoutes.Home,
        labelRes = R.string.home,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
    )

    data object Favorites : BottomBarItems(
        route = MainRoutes.Favorites,
        labelRes = R.string.favorite,
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Outlined.FavoriteBorder,
    )

    data object Settings : BottomBarItems(
        route = MainRoutes.Settings,
        labelRes = R.string.settings,
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
    )
}
