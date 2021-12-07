package com.example.to_docompose.ui.screens.list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.example.to_docompose.R
import com.example.to_docompose.components.PriorityItem
import com.example.to_docompose.data.models.Priority
import com.example.to_docompose.ui.theme.*
import com.example.to_docompose.ui.viewmodels.SharedViewModel
import com.example.to_docompose.util.SearchAppBarState
import com.example.to_docompose.util.TrailingIconState

@Composable
fun ListAppBar(
    sharedViewModel: SharedViewModel,
    searchAppBarState: SearchAppBarState,
    searchTextState: String
){
    when(searchAppBarState){
        SearchAppBarState.CLOSED->{
        DefaultListAppBar(
            onSearchClicked = {
                             sharedViewModel.searchAppBarState.value=
                                 SearchAppBarState.OPENED
            },
            onShortClicked ={},
            onDeleteClicked = {})
        }
        else->{
            SearchAppBar(text = searchTextState,
                onTextChange = { newText->
                    sharedViewModel.searchTextState.value=newText},
                onCloseClicked = {
                                 sharedViewModel.searchAppBarState.value=
                                     SearchAppBarState.CLOSED
                        sharedViewModel.searchTextState.value=""
                },
                onSearchClicked = {})
        }
    }


}

@Composable
fun DefaultListAppBar(
    onSearchClicked: () -> Unit,
    onShortClicked:(Priority)->Unit,
    onDeleteClicked: () -> Unit){
    TopAppBar(
        title = {
        Text(text = stringResource(R.string.list_screen_title), color = MaterialTheme.colors.topAppBarContentColor)},
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
    IconButton(onClick = { onSearchClicked() }) {
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
fun SearchAppBar(text:String,
              onTextChange:(String)->Unit,
              onCloseClicked:()->Unit,
              onSearchClicked: (String) -> Unit){
    var trailingIconState by remember { mutableStateOf(TrailingIconState.READY_TO_DELETE)}

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(TOP_APP_BAR_HEIGHT),
        elevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colors.topAppBarBackgroundColor) {
        TextField(
            modifier = Modifier.fillMaxWidth(),

            value = text,
            onValueChange ={ onTextChange(it)},
            placeholder = {
                Text(
                    modifier = Modifier.alpha(ContentAlpha.medium),
                    text = stringResource(R.string.search_string),
                    color = Color.White) },
            textStyle = TextStyle(
                    color = MaterialTheme.colors.topAppBarContentColor,
                    fontSize = MaterialTheme.typography.subtitle1.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(onClick = { },modifier = Modifier.alpha(ContentAlpha.disabled)) {
                    Icon(imageVector =Icons.Filled.Search ,
                        contentDescription = stringResource(R.string.search_icon) ,
                        tint = MaterialTheme.colors.topAppBarContentColor)
                }
            },
            trailingIcon = {
                IconButton(onClick = {
                    when(trailingIconState){
                        TrailingIconState.READY_TO_DELETE->{
                            onTextChange("")
                            trailingIconState=TrailingIconState.READY_TO_CLOSE
                        }
                        TrailingIconState.READY_TO_CLOSE->{
                            if(text.isNotEmpty()){
                                onTextChange("")
                            }
                            else{
                                onCloseClicked()
                                trailingIconState=TrailingIconState.READY_TO_DELETE
                            }

                        }
                    }
                }) {
                    Icon(imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(R.string.close_icon),
                        tint =MaterialTheme.colors.topAppBarContentColor )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClicked(text)
                }
            ),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = MaterialTheme.colors.topAppBarContentColor,
                disabledIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                backgroundColor = Color.Transparent
            )

        )
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

@Composable
@Preview
private fun SearchAppBarPreview(){
    SearchAppBar(text ="" ,
        onTextChange ={} ,
        onCloseClicked ={},
        onSearchClicked = {} )


}
