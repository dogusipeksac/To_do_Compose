package com.example.to_docompose.ui.screens.list

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.to_docompose.data.models.Priority
import com.example.to_docompose.data.models.ToDoTask
import com.example.to_docompose.ui.theme.*
import com.example.to_docompose.util.RequestState
import com.example.to_docompose.util.SearchAppBarState

@ExperimentalMaterialApi
@Composable
fun ListContent(
    searchAppBarState:SearchAppBarState,
    searchedTasks: RequestState<List<ToDoTask>>,
    lowPriorityTasks:List<ToDoTask>,
    highPriorityTasks:List<ToDoTask>,
    sortState:RequestState<Priority>,
    allTasks: RequestState<List<ToDoTask>>,
    navigateToDoTaskScreen: (taskId:Int)->Unit){
    if(sortState is RequestState.Success){
        when{
            //aranmış olanı
            searchAppBarState==SearchAppBarState.TRIGGERED->{
                if(searchedTasks is RequestState.Success){
                    HandleListContent(
                        tasks = searchedTasks.data,
                        navigateToTaskScreen =navigateToDoTaskScreen )
                }
            }
            //none ise oan göre sıralıyor
            sortState.data==Priority.NONE->{
                if(allTasks is RequestState.Success){
                    HandleListContent(tasks = allTasks.data,
                        navigateToTaskScreen = navigateToDoTaskScreen)
                }
            }
            //low oalrak sıralıyor
            sortState.data==Priority.LOW ->{
                HandleListContent(tasks = lowPriorityTasks,
                    navigateToTaskScreen = navigateToDoTaskScreen)
            }
            //high olarak sıralıyor
            sortState.data==Priority.HIGH->{
                HandleListContent(tasks = highPriorityTasks,
                    navigateToTaskScreen = navigateToDoTaskScreen)
            }
        }

    }

}
@ExperimentalMaterialApi
@Composable
fun HandleListContent(
    tasks:List<ToDoTask>,
    navigateToTaskScreen:(tasks:Int)->Unit
){
    if(tasks.isEmpty()){
        EmptyContent()
    }
    else{
        DisplayTasks(
            tasks = tasks,
            navigateToDoTaskScreen = navigateToTaskScreen
        )
    }
}

@ExperimentalMaterialApi
@Composable
fun DisplayTasks(tasks: List<ToDoTask>,
                navigateToDoTaskScreen: (taskId:Int)->Unit){
    LazyColumn {
        items(
            items=tasks,key={task->
                task.id
            }){task->
            TaskItem(
                toDoTask = task,
                navigateToDoTaskScreen = navigateToDoTaskScreen
            )
        }
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