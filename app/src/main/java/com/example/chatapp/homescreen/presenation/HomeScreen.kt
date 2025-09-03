package com.example.chatapp.homescreen.presenation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.chatapp.R
import com.example.chatapp.ui.theme.Black
import com.example.chatapp.ui.theme.Blue
import com.example.chatapp.ui.theme.Gray100
import com.example.chatapp.ui.theme.Inter
import com.example.chatapp.ui.theme.Inter13
import com.example.chatapp.ui.theme.Inter14
import com.example.chatapp.ui.theme.Inter17
import com.example.chatapp.ui.theme.Inter30
import com.example.chatapp.ui.theme.White


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(onEvent : (HomeEvent) -> Unit = {}, onUserClick: (String) -> Unit,state : HomeState = HomeState()) {


    val screenWidth = LocalConfiguration.current.screenWidthDp
    val myImageWidth = screenWidth / 9.3
    val userImagesWidth = screenWidth / 6.25


    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues)
                .systemBarsPadding()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberAsyncImagePainter(state.myData?.photo),
                    contentDescription = "my profile",
                    modifier = Modifier
                        .size(myImageWidth.dp)
                        .background(
                            Color.Black,
                            RoundedCornerShape(100.dp)
                        )
                        .clip(RoundedCornerShape(100.dp)), contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Chats",
                    style = Inter30,
                    color = White,
                    lineHeight = 38.sp,
                    letterSpacing = 0.4.sp
                )


            }
            Spacer(modifier = Modifier.height(14.dp))
            Box(contentAlignment = Alignment.CenterStart) {

                if (state.searchText.isEmpty()) {
                    Text(
                        text = "Search",
                        color = Gray100,
                        fontSize = 16.sp, modifier = Modifier.padding(start = 35.dp)
                    )
                }

                BasicTextField(
                    value = state.searchText,
                    onValueChange = { onEvent(HomeEvent.OnSearchTextChanged(it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(50.dp)

                        .background(White.copy(0.2f), shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 9.dp),
                    singleLine = true,
                    textStyle = TextStyle(color = Gray100, fontSize = 17.sp, fontFamily = Inter),
                    cursorBrush = SolidColor(Gray100),
                    decorationBox = { innerTextField ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_search),
                                contentDescription = "search",
                                tint = Color.Unspecified
                            )
                            Spacer(modifier = Modifier.width(6.dp))


                            innerTextField()
                        }


                    }
                )
            }


            Spacer(modifier = Modifier.height(20.dp))

            if (state.users.any { it.isOnline }) {
                LazyRow {
                    items(state.users.filter { it.isOnline }) { user ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box {
                                Image(
                                    painter = rememberAsyncImagePainter(user.photo),
                                    contentDescription = "user profile",
                                    modifier = Modifier
                                        .size(myImageWidth.dp)
                                        .clip(RoundedCornerShape(100.dp)),
                                    contentScale = ContentScale.Crop
                                )

                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .background(Black, RoundedCornerShape(100.dp))
                                        .padding(3.dp)
                                        .background(Blue, RoundedCornerShape(100.dp))
                                        .align(Alignment.BottomEnd)
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = user.username,
                                style = Inter13,
                                color = White.copy(0.35f),
                                lineHeight = 18.sp,
                                letterSpacing = (-0.08).sp
                            )
                        }

                        Spacer(modifier = Modifier.width(28.dp))
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "There are no online users",
                        style = Inter14,
                        color = White.copy(0.5f),
                        lineHeight = 18.sp,
                        letterSpacing = (-0.08).sp
                    )
                }
            }




            Spacer(modifier = Modifier.height(23.dp))

            val users = if (state.searchText.isEmpty()) state.users else state.filteredUsers

            LazyColumn(modifier = Modifier.fillMaxWidth()) {

                items(users) { user ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onUserClick(user.id)
                                    onEvent(HomeEvent.MarkMessagesAsRead(user.id))
                            }
                            .padding(vertical = 8.dp)
                    ) {
                        Box {
                            Image(
                                painter = rememberAsyncImagePainter(user.photo),
                                contentDescription = "user profile",
                                modifier = Modifier
                                    .size(userImagesWidth.dp)
                                    .clip(RoundedCornerShape(100.dp)),
                                contentScale = ContentScale.Crop
                            )

                            if (user.messageCount > 0) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .background(Blue, RoundedCornerShape(50))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = user.messageCount.toString(),
                                        style = Inter13,
                                        color = White
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = user.username,
                                style = Inter17,
                                color = White
                            )
                            Text(
                                text = user.lastMessage ?: "",
                                style = Inter14,
                                color = White.copy(0.5f)
                            )
                        }

                        Text(
                            text = user.lastTime ?: "",
                            style = Inter14,
                            color = Gray100
                        )
                    }
                }

            }


        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(onUserClick = {
    })
}