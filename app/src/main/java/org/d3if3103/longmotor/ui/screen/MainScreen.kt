package org.d3if3103.longmotor.ui.screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3103.longmotor.R
import org.d3if3103.longmotor.ui.theme.LongMotorTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(onClick = {
                        navController.navigate(org.d3if3103.longmotor.Navigation.Screen.About.route)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(id = R.string.tentang_aplikasi),
                            tint = MaterialTheme.colorScheme.primary
                        )

                    }
                }
            )
        }
    ) { padding ->
        ScreenContent(Modifier.padding(padding))
    }
}

@SuppressLint("StringFormatMatches")
@Composable

fun ScreenContent(modifier: Modifier) {
    var nomor by rememberSaveable { mutableStateOf("") }
    var nomorError by rememberSaveable {
        mutableStateOf(false)
    }


    val radioOptions = listOf(

        stringResource(id = R.string.cc_motor_down),
        stringResource(id = R.string.cc_motor_up),


        )
    var cc by rememberSaveable { mutableStateOf(radioOptions[0]) }
    var hargaCucian by rememberSaveable { mutableStateOf(0) }

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally


    ) {
        Image(
            painter = painterResource
                (id = R.drawable.logo),
            contentDescription = "logo"
        )

        Text(
            text = stringResource(id = R.string.intro_long),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )

        var nomorError by rememberSaveable { mutableStateOf(false) }
        OutlinedTextField(
            value = nomor,
            trailingIcon = {
                IconPicker(isError = nomorError, unit ="" )
            },
            supportingText = {
                             ErrorHint(isError =nomorError )
            },
            onValueChange = {
                nomor = it
                nomorError = it.isBlank() // Set error if input is empty
            },
            label = { Text(text = stringResource(id = R.string.nomor_plat)) },
            isError = nomorError,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier
                .padding(top = 6.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
        ) {
            radioOptions.forEach { text ->
                ccOptions(
                    label = text,
                    isSelected = cc == text,
                    modifier = Modifier
                        .selectable(
                            selected = cc == text,
                            onClick = { cc = text },
                            role = Role.RadioButton
                        )
                        .weight(1f)
                        .padding(16.dp)
                )
            }
        }


        Button(
            onClick = {
                nomorError = (nomor == "" || nomor == "0")
                if (nomorError)return@Button


                if (nomor.isNotBlank()) {
                    hargaCucian = if (cc == "Down 150cc") {
                        15000
                    } else {
                        25000
                    }
                } else {

                    hargaCucian = 0
                }
            },
            modifier = Modifier.padding(top = 8.dp),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
        ) {
            Text(text = stringResource(R.string.hitung))
        }

        Button(
            onClick = {
                shareData(
                    context = context,
                    message = context.getString(
                        R.string.bagikan_template,
                        nomor, cc, hargaCucian
                    )
                )
            },
            modifier = Modifier.padding(top = 0.dp),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
        ) {
            Text(text = stringResource(R.string.bagikan))
        }



        Text(
            text = "Laundry Prices: Rp.$hargaCucian", // Display the calculated laundry price
            style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}




        @Composable
fun ccOptions(label: String, isSelected: Boolean, modifier: Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = isSelected, onClick = null)
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
    }

}

@Composable
fun IconPicker(isError: Boolean, unit: String) {
    if (isError) {

        Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
    } else {
        Text(text = unit)

    }

}

@Composable

fun ErrorHint(isError: Boolean) {
    if (isError) {
        Text(text = stringResource(R.string.input_valid))

    }

}


private fun shareData(context: Context, message: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if (shareIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(shareIntent)
    }

}


@Preview(showBackground = true)
@Composable
fun ScreenPreview() {
    LongMotorTheme {
        MainScreen(rememberNavController())


    }
}