package org.d3if3103.longmotor.Navigation

    sealed class Screen (val route: String) {
        data object Home : Screen("mainScreen")
       data object About : Screen("aboutScreen")

    }
