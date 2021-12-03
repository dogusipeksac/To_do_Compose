package com.example.to_docompose.ui.screens.list

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.to_docompose.R
import com.example.to_docompose.components.PriorityItem
import com.example.to_docompose.data.models.Priority
import com.example.to_docompose.ui.theme.LARGE_PADDING
import com.example.to_docompose.ui.theme.Typography
import com.example.to_docompose.ui.theme.topAppBarBackgroundColor
import com.example.to_docompose.ui.theme.topAppBarContentColor

@Composable
fun ListAppBar(){
    DefaultListAppBar(
        onSearchClicked = {},
        onShortClicked ={},
        onDeleteClicked = {}
    )
}

@Composable
fun DefaultListAppBar(
    onSearchClicked: () -> Unit,
    onShortClicked:(Priority)->Unit,
    onDeleteClicked: () -> Unit){
    TopAppBar(
        title = {
        Text(text = "Tasks", color = MaterialTheme.colors.topAppBarContentColor)},
        actions={
            ListAppBarActions(
                onSearchClicked = onSearchClicked,
                onShortClicked =onShortClicked,
                onDeleteClicked = onDeleteClicked )
        },
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor)
}
@Composable
fun ListAppBarActions(
    onSearchClicked: () -> Unit,
    onShortClicked:(Priority)->Unit,
    onDeleteClicked: () -> Unit){
    SearchAction(onSearchClicked = onSearchClicked)
    SortAction(onSortClicked = onShortClicked)
    DeleteAllAction(onDeleteClicked = onDeleteClicked)
}
@Composable
fun SearchAction(onSearchClicked: () -> Unit){
    IconButton(onClick = { onSearchClicked }) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = stringResource(id=R.string.search_tasks),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun SortAction(onSortClicked:(Priority)-> Unit){
    var expanded by remember{ mutableStateOf(false) }
    IconButton(onClick = {  expanded =true}) {
        Icon(painter = painterResource(id = R.drawable.ic_filter_list),
            contentDescription = stringResource(R.string.sort_action),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
        DropdownMenu(
            expanded = expanded,
            //boş bir yere tıklanınca
            onDismissRequest = { expanded=false }) {
            //low
            DropdownMenuItem(
                onClick = {
                    expanded=false
                    onSortClicked(Priority.LOW)
                }
            ) {
                PriorityItem(priority = Priority.LOW)
            }
            //high
            DropdownMenuItem(
                onClick = {
                    expanded=false
                    onSortClicked(Priority.HIGH)

                }
            )
            {
                PriorityItem(priority = Priority.HIGH)
            }
            //none
            DropdownMenuItem(
                onClick = {
                    expanded=false
                    onSortClicked(Priority.NONE)
                }
            )
            {
                PriorityItem(priority = Priority.NONE)
            }
        }

    }
}

@Composable
fun DeleteAllAction(onDeleteClicked:()-> Unit){
    var expanded by remember{ mutableStateOf(false) }
    IconButton(onClick = {  expanded =true}) {
        Icon(painter = painterResource(id = R.drawable.ic_more_vert),
            contentDescription = stringResource(R.string.delete_all_tasks),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
        DropdownMenu(
            expanded = expanded,
            //boş bir yere tıklanınca
            onDismissRequest = { expanded=false }) {
            DropdownMenuItem(
                onClick = {
                    expanded=false
                    onDeleteClicked()
                }
            )
            {
                Text(
                    modifier = Modifier.padding(start = LARGE_PADDING),
                    text = stringResource(R.string.delete_all_tasks),
                    style = Typography.subtitle2)
            }
        }

    }
}

@Composable
@Preview
private fun DefaultListAppPreview(){
    DefaultListAppBar(
        onSearchClicked = {},
        onShortClicked = {},
        onDeleteClicked = {})
}
