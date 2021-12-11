package com.example.to_docompose.ui.screens.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.to_docompose.R
import com.example.to_docompose.data.models.Priority
import com.example.to_docompose.data.models.ToDoTask
import com.example.to_docompose.ui.theme.*
import com.example.to_docompose.util.Action
import com.example.to_docompose.util.RequestState
import com.example.to_docompose.util.SearchAppBarState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun ListContent(
    searchAppBarState:SearchAppBarState,
    searchedTasks: RequestState<List<ToDoTask>>,
    lowPriorityTasks:List<ToDoTask>,
    highPriorityTasks:List<ToDoTask>,
    sortState:RequestState<Priority>,
    onSwipeToDelete:(Action,ToDoTask)->Unit,
    allTasks: RequestState<List<ToDoTask>>,
    navigateToDoTaskScreen: (taskId:Int)->Unit){
    if(sortState is RequestState.Success){
        when{
            //aranmış olanı
            searchAppBarState==SearchAppBarState.TRIGGERED->{
                if(searchedTasks is RequestState.Success){
                    HandleListContent(
                        tasks = searchedTasks.data,
                        onSwipeToDelete = onSwipeToDelete,
                        navigateToTaskScreen =navigateToDoTaskScreen )
                }
            }
            //none ise oan göre sıralıyor
            sortState.data==Priority.NONE->{
                if(allTasks is RequestState.Success){
                    HandleListContent(tasks = allTasks.data,
                        onSwipeToDelete = onSwipeToDelete,
                        navigateToTaskScreen = navigateToDoTaskScreen)
                }
            }
            //low oalrak sıralıyor
            sortState.data==Priority.LOW ->{
                HandleListContent(tasks = lowPriorityTasks,
                    onSwipeToDelete = onSwipeToDelete,
                    navigateToTaskScreen = navigateToDoTaskScreen)
            }
            //high olarak sıralıyor
            sortState.data==Priority.HIGH->{
                HandleListContent(tasks = highPriorityTasks,
                    onSwipeToDelete = onSwipeToDelete,
                    navigateToTaskScreen = navigateToDoTaskScreen,
                )
            }
        }

    }

}
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun HandleListContent(
    tasks:List<ToDoTask>,
    onSwipeToDelete:(Action,ToDoTask)->Unit,
    navigateToTaskScreen:(tasks:Int)->Unit
){
    if(tasks.isEmpty()){
        EmptyContent()
    }
    else{
        DisplayTasks(
            tasks = tasks,
            onSwipeToDelete = onSwipeToDelete,
            navigateToDoTaskScreen = navigateToTaskScreen
        )
    }
}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun DisplayTasks(tasks: List<ToDoTask>,
                navigateToDoTaskScreen: (taskId:Int)->Unit,
                onSwipeToDelete:(Action,ToDoTask)->Unit){
    LazyColumn {
        items(
            items=tasks,key={task->
                task.id
            }){task->
            val dismissState= rememberDismissState()
            val dismissDirection=dismissState.dismissDirection
            val isDismissed=dismissState.isDismissed(DismissDirection.EndToStart)
            if(isDismissed && dismissDirection==DismissDirection.EndToStart){
                val scope= rememberCoroutineScope()
                scope.launch {
                    delay(300)
                    onSwipeToDelete(Action.DELETE,task)
                }

            }
            val degrees by animateFloatAsState(
            if (dismissState.targetValue == DismissValue.Default)
                0f
            else
                -45f
            )
            var itemApperred by remember{ mutableStateOf(false) }
            LaunchedEffect(key1 = true ){
                itemApperred=true
            }
            AnimatedVisibility(
                visible = itemApperred && !isDismissed,
                enter = expandVertically(
                    animationSpec = tween(
                        durationMillis = 300
                    )
                ),
                exit = shrinkVertically(
                    animationSpec = tween(
                        durationMillis = 300
                    )
                )
            ) {
                SwipeToDismiss(
                    state =dismissState,
                    directions = setOf(DismissDirection.EndToStart),
                    dismissThresholds ={
                        FractionalThreshold(fraction = 0.2f)
                    } ,
                    background ={ ReadBackground(degrees =degrees )},
                    dismissContent = {
                        TaskItem(toDoTask = task,
                            navigateToDoTaskScreen =navigateToDoTaskScreen
                        )
                    }

                )
            }
        }
    }
}

@Composable
fun ReadBackground(degrees:Float){
    Box(modifier = Modifier
        .fillMaxSize()
        .background(HighPriorityColor)
        .padding(horizontal = LARGES_PADDING),
    contentAlignment = Alignment.CenterEnd){
        Icon(
            modifier = Modifier.rotate(degrees = degrees),
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(id = R.string.delete_icon),
        tint = Color.White
        )
    }
}

@ExperimentalMaterialApi
@Composable
fun TaskItem(toDoTask: ToDoTask,
             navigateToDoTaskScreen: (taskId:Int)->Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colors.taskItemBackgroundColor,
        shape = RectangleShape,
        elevation = TASK_ITEM_ELEVATION,
        onClick = {
            navigateToDoTaskScreen(toDoTask.id)
        }
    ) {
        Column(modifier = Modifier
            .padding(all = LARGE_PADDING)
            .fillMaxWidth()) {
            Row {
               Text(
                   modifier =Modifier.weight(8f) ,
                   text = toDoTask.title,
                   color = MaterialTheme.colors.taskItemTextColor,
                   style = MaterialTheme.typography.h5,
                   fontWeight = FontWeight.Bold,
                   maxLines = 1
               )
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.TopEnd,){
                    Canvas(
                        modifier =
                        Modifier.size(PRIORITY_INDICATOR_SIZE),

                    ){
                            drawCircle(color = toDoTask.priority.color
                            )
                    }

                }
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = toDoTask.description,
                style= MaterialTheme.typography.subtitle1,
                color =MaterialTheme.colors.taskItemTextColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis

            )
        }

    }
}

@ExperimentalMaterialApi
@Composable
@Preview
fun textItemPreview(){
    TaskItem(toDoTask = ToDoTask(
        id = 0,
        title="Title",
        description = "Some random text",
        priority = Priority.MEDIUM),
        navigateToDoTaskScreen ={} )
}

@Composable
@Preview
private fun RedBackgroundPreview(){
    Column(modifier = Modifier.height(100.dp)) {
        ReadBackground(degrees = 0f)
        
    }
}