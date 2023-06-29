package com.example.composenavigation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.composenavigation.ui.theme.ComposeNavigationTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeNavigationTheme {
                var title by rememberSaveable { mutableStateOf("") }
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                LaunchedEffect(key1 = navBackStackEntry) {
                    Log.d("TAG currentBackStack",
                        navController.currentBackStack.value.mapNotNull { it.destination.route }
                            .joinToString()
                    )
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(title = { Text(text = title) })
                    },
                    bottomBar = {
                        NavigationBar {
                            TabDestinations.values().forEach { tab ->
                                NavigationBarItem(
                                    selected = currentDestination?.hierarchy?.any { it.route == tab.route } == true,
                                    onClick = {
                                        title = tab.title
                                        navController.navigate(tab.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = tab.icon,
                                            contentDescription = tab.title
                                        )
                                    },
                                    label = {
                                        Text(text = tab.title)
                                    }
                                )
                            }
                        }
                    },
                ) { paddingValues ->
                    NavHost(
                        modifier = Modifier.padding(paddingValues),
                        navController = navController,
                        startDestination = TabDestinations.Home.route,
                    ) {
                        mainTabsNavigationGraph(navController)
                    }
                }
            }
        }
    }
}

enum class TabDestinations(val route: String, val icon: ImageVector, val title: String) {
    Home("home", Icons.Default.Home, title = "Home"),
    Map("map", Icons.Default.LocationOn, title = "Map"),
    Car("car", Icons.Default.ShoppingCart, title = "Car"),
    Support("support", Icons.Default.Call, title = "Support"),
    Profile("profile", Icons.Default.AccountCircle, title = "Profile"),
}

enum class HomeDestinations(val route: String, val title: String, val nextDestination: String) {
    Screen1("home/screen1", "Home Screen One", "home/screen2"),
    Screen2("home/screen2", "Home Screen Two", "home/screen3"),
    Screen3("home/screen3", "Home Screen Three", "home/screen4"),
    Screen4("home/screen4", "Home Screen Four", NA_ROUTE),
}

enum class MapDestinations(val route: String, val title: String, val nextDestination: String) {
    Screen1("map/screen1", "Map Screen One", "map/screen2"),
    Screen2("map/screen2", "Map Screen Two", "map/screen3"),
    Screen3("map/screen3", "Map Screen Three", "map/screen4"),
    Screen4("map/screen4", "Map Screen Four", NA_ROUTE),
}

enum class CarDestinations(val route: String, val title: String, val nextDestination: String) {
    Screen1("car/screen1", "Car Screen One", "car/screen2"),
    Screen2("car/screen2", "Car Screen Two", "car/screen3"),
    Screen3("car/screen3", "Car Screen Three", "car/screen4"),
    Screen4("car/screen4", "Car Screen Four", NA_ROUTE),
}

enum class SupportDestinations(val route: String, val title: String, val nextDestination: String) {
    Screen1("support/screen1", "Support Screen One", "support/screen2"),
    Screen2("support/screen2", "Support Screen Two", "support/screen3"),
    Screen3("support/screen3", "Support Screen Three", "support/screen4"),
    Screen4("support/screen4", "Support Screen Four", NA_ROUTE),
}

enum class ProfileDestinations(val route: String, val title: String, val nextDestination: String) {
    Screen1("profile/screen1", "Profile Screen One", "profile/screen2"),
    Screen2("profile/screen2", "Profile Screen Two", "profile/screen3"),
    Screen3("profile/screen3", "Profile Screen Three", "profile/screen4"),
    Screen4("profile/screen4", "Profile Screen Four", NA_ROUTE),
}

const val NA_ROUTE = "na"

fun NavGraphBuilder.mainTabsNavigationGraph(navController: NavController) {
    navigation(
        route = TabDestinations.Home.route,
        startDestination = HomeDestinations.Screen1.route
    ) {
        homeNavigationGraph(navController)
    }
    navigation(
        route = TabDestinations.Map.route,
        startDestination = MapDestinations.Screen1.route
    ) {
        mapNavigationGraph(navController)
    }
    navigation(
        route = TabDestinations.Car.route,
        startDestination = CarDestinations.Screen1.route
    ) {
        carNavigationGraph(navController)
    }
    navigation(
        route = TabDestinations.Support.route,
        startDestination = SupportDestinations.Screen1.route
    ) {
        supportNavigationGraph(navController)
    }
    navigation(
        route = TabDestinations.Profile.route,
        startDestination = ProfileDestinations.Screen1.route
    ) {
        profileNavigationGraph(navController)
    }
    dialog(NA_ROUTE) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Text(
                modifier = Modifier.padding(24.dp),
                text = "End of the journey",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

fun NavGraphBuilder.homeNavigationGraph(navController: NavController) {
    HomeDestinations.values().forEach { homeDestination ->
        composable(homeDestination.route) {
            Screen(header = homeDestination.title) {
                navController.navigate(homeDestination.nextDestination)
            }
        }
    }
}

fun NavGraphBuilder.mapNavigationGraph(navController: NavController) {
    MapDestinations.values().forEach { mapDestination ->
        composable(mapDestination.route) {
            Screen(header = mapDestination.title) {
                navController.navigate(mapDestination.nextDestination)
            }
        }
    }
}

fun NavGraphBuilder.carNavigationGraph(navController: NavController) {
    CarDestinations.values().forEach { carDestination ->
        composable(carDestination.route) {
            Screen(header = carDestination.title) {
                navController.navigate(carDestination.nextDestination)
            }
        }
    }
}

fun NavGraphBuilder.supportNavigationGraph(navController: NavController) {
    SupportDestinations.values().forEach { supportDestination ->
        composable(supportDestination.route) {
            Screen(header = supportDestination.title) {
                navController.navigate(supportDestination.nextDestination)
            }
        }
    }
}

fun NavGraphBuilder.profileNavigationGraph(navController: NavController) {
    ProfileDestinations.values().forEach { profileDestination ->
        composable(profileDestination.route) {
            Screen(header = profileDestination.title) {
                navController.navigate(profileDestination.nextDestination)
            }
        }
    }
}

@Composable
fun Screen(header: String, onClicked: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = header,
            style = MaterialTheme.typography.headlineLarge
        )
        Button(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp)
                .fillMaxWidth(),
            onClick = onClicked
        ) {
            Text(text = "Next")
        }
    }
}