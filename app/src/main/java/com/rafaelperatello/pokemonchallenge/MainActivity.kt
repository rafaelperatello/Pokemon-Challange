package com.rafaelperatello.pokemonchallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.Keep
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.coroutineScope
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rafaelperatello.pokemonchallenge.ui.screen.home.HomeScreen
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
                PokemonScaffold()
            }
        }
    }
}

@Composable
fun PokemonScaffold() {
    val navController = rememberNavController()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute: State<MainRoutes?> = remember {
        derivedStateOf {
            currentBackStackEntry?.destination?.route?.let { route ->
                when (route) {
                    MainRoutes.Home::class.qualifiedName -> MainRoutes.Home
                    MainRoutes.Favorites::class.qualifiedName -> MainRoutes.Favorites
                    MainRoutes.Settings::class.qualifiedName -> MainRoutes.Settings
                    else -> null
                }
            }
        }
    }

    val appBarMenuActions: MutableState<AppBarAction?> = remember {
        mutableStateOf(null)
    }
    val onUpdateAppBarAction: (AppBarAction?) -> Unit = { action ->
        appBarMenuActions.value = action
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            PokemonAppBar(
                title = stringResource(id = R.string.title_main),
                action = appBarMenuActions
            )
        },
        bottomBar = {
            HomeBottomBar(navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            MainNavHost(
                navController = navController,
                startDestination = MainRoutes.Home,
                currentRoute = currentRoute,
                onUpdateAppBarAction = onUpdateAppBarAction
            )
        }
    }
}

@Composable
internal fun MainNavHost(
    navController: NavHostController,
    startDestination: MainRoutes,
    currentRoute: State<MainRoutes?>,
    onUpdateAppBarAction: (AppBarAction?) -> Unit = {}
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<MainRoutes.Home>() {
            HomeScreen(
                currentRoute = currentRoute,
                onUpdateAppBarAction = onUpdateAppBarAction
            )
        }
        composable<MainRoutes.Favorites>() {
            if (currentRoute.value == MainRoutes.Favorites) {
                onUpdateAppBarAction(null)
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Favorites")
            }
        }
        composable<MainRoutes.Settings>() {
            if (currentRoute.value == MainRoutes.Settings) {
                onUpdateAppBarAction(null)
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Settings")
            }
        }
    }
}

@Composable
private fun HomeBottomBar(
    navController: NavHostController
) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.primary,
        modifier = Modifier.windowInsetsPadding(
            WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)
        )
    ) {
        mainScreens.forEach { destination ->
            val selected =
                currentDestination?.hierarchy?.any {
                    it.route == destination.route::class.qualifiedName.orEmpty()
                } == true

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(destination.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    val icon =
                        if (selected) destination.selectedIcon
                        else destination.unselectedIcon

                    Icon(
                        imageVector = icon,
                        modifier = Modifier.size(16.dp),
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = destination.labelRes),
                        fontSize = 10.sp
                    )
                }
            )
        }
    }
}

private val mainScreens = listOf(
    BottomBarItems.Home,
    BottomBarItems.Favorites,
    BottomBarItems.Settings
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
    val unselectedIcon: ImageVector
) {

    data object Home : BottomBarItems(
        route = MainRoutes.Home,
        labelRes = R.string.home,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )

    data object Favorites : BottomBarItems(
        route = MainRoutes.Favorites,
        labelRes = R.string.favorite,
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Outlined.FavoriteBorder
    )

    data object Settings : BottomBarItems(
        route = MainRoutes.Settings,
        labelRes = R.string.settings,
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )
}
