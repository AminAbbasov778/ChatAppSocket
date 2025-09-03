package com.example.chatapp.chatscreen.presenation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.chatapp.R
import com.example.chatapp.core.presentation.utils.toReadableTime
import com.example.chatapp.ui.theme.Black
import com.example.chatapp.ui.theme.Blue
import com.example.chatapp.ui.theme.Gray100
import com.example.chatapp.ui.theme.Inter
import com.example.chatapp.ui.theme.Inter13
import com.example.chatapp.ui.theme.Inter17
import com.example.chatapp.ui.theme.White
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatScreen(
    recieverId: String,
    onBackCLick: () -> Unit,
    state : ChatState = ChatState(),onEvent: (ChatEvent) -> Unit,
) {

    val recieverImageWidth = LocalConfiguration.current.screenWidthDp / 10.4
    val chatTextPadding = LocalConfiguration.current.screenWidthDp / 3.6
    val recieverImageSizeInAnswer = LocalConfiguration.current.screenWidthDp / 13.39
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        onEvent(ChatEvent.ClearState)
        onEvent(ChatEvent.EnterChat(recieverId))
    }
    val density = LocalDensity.current
    var isKeyboardVisible by remember { mutableStateOf(false) }



    val imeBottomPx = WindowInsets.ime.getBottom(density)

    LaunchedEffect(imeBottomPx) {
        isKeyboardVisible = imeBottomPx > 0
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }
    LaunchedEffect(imeBottomPx) {
        isKeyboardVisible = imeBottomPx > 0

        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }
    LaunchedEffect(Unit) {
        onEvent(ChatEvent.ObserveIncomingMessages(recieverId))
    }



    Scaffold(bottomBar = {
        Row(
            Modifier.fillMaxWidth()

            . imePadding ()
            .padding(horizontal = 12.dp).padding(bottom = 30.dp),

        verticalAlignment = Alignment.CenterVertically
        ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(White.copy(0.2f), shape = RoundedCornerShape(18.dp))
                .padding(horizontal = 8.dp, vertical = 9.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (state.messageText.isEmpty()) {
                Text(
                    text = "Message",
                    color = Gray100,
                    fontSize = 16.sp,
                )
            }

            BasicTextField(
                value = state.messageText,
                onValueChange = { onEvent(ChatEvent.UpdateText(it)) },
                textStyle = TextStyle(
                    color = Gray100,
                    fontSize = 17.sp,
                    fontFamily = Inter,
                ),
                cursorBrush = SolidColor(Gray100),
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 40.dp)
                    .align(Alignment.CenterStart)
            ) { innerTextField ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    innerTextField()
                }
            }
        }

        Spacer(modifier = Modifier.width(13.dp))
        Icon(
            painter = painterResource(R.drawable.ic_send),
            contentDescription = "send",
            tint = Color.Unspecified,
            modifier = Modifier.clickable {
                Log.d("chatscreen", "onEvent: ")

               onEvent(ChatEvent.SendMessage(state.messageText, recieverId))
            }
        )
    }
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Black)
                .systemBarsPadding()
                .padding(paddingValues)
                .padding(horizontal = 12.dp)
        ) {

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = "back",
                    tint = Color.Unspecified, modifier = Modifier.clickable {
                        onBackCLick()
                    }
                )
                Spacer(modifier = Modifier.width(10.dp))

                Image(
                    painter = rememberAsyncImagePainter(state.sender?.photo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(recieverImageWidth.dp)
                        .clip(RoundedCornerShape(100.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = state.sender?.username ?: "",
                        style = Inter17,
                        color = White,
                        lineHeight = 22.sp,
                        letterSpacing = (-0.4).sp
                    )
                    Text(
                        text = if (state.sender?.isOnline == true) "Online" else "Offline",
                        style = Inter13,
                        color = White.copy(0.5f),
                        lineHeight = 20.sp,
                        letterSpacing = (-0.08).sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))




            LazyColumn(
                state = listState,
                reverseLayout = false,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.weight(1f)
            ) {
                val messages = state.messages

                itemsIndexed(
                    messages,
                    key = { index, msg -> msg.timestamp ?: index }) { index, msg ->
                    val prevMsg = messages.getOrNull(index - 1)
                    val nextMsg = messages.getOrNull(index + 1)


                    val isBlockStart = prevMsg?.isMine != msg.isMine || prevMsg == null ||
                            msg.isTimeVisible

                    val isBlockEnd = nextMsg?.isMine != msg.isMine || nextMsg == null

                    val shape = when {
                        isBlockStart && isBlockEnd -> if (msg.isMine) RoundedCornerShape(
                            18.dp, 18.dp, 4.dp, 18.dp
                        ) else RoundedCornerShape(4.dp, 18.dp, 18.dp, 18.dp)

                        isBlockStart -> if (msg.isMine) RoundedCornerShape(
                            18.dp, 18.dp, 4.dp, 18.dp
                        ) else RoundedCornerShape(18.dp, 18.dp, 4.dp, 18.dp)

                        isBlockEnd -> if (msg.isMine) RoundedCornerShape(
                            18.dp, 4.dp, 18.dp, 18.dp
                        ) else RoundedCornerShape(4.dp, 18.dp, 18.dp, 18.dp)

                        else -> RoundedCornerShape(18.dp)
                    }


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = if (msg.isMine) chatTextPadding.dp else 0.dp,
                                end = if (!msg.isMine) chatTextPadding.dp else 0.dp
                            ),
                        horizontalArrangement = if (msg.isMine) Arrangement.End else Arrangement.Start
                    ) {
                        if (msg.isMine) {
                            Box(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .clip(shape)
                                    .background(Blue)
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = msg.message,
                                    style = Inter17,
                                    color = White,
                                    letterSpacing = (-0.41).sp
                                )
                            }
                        } else {
                            Row {

                                if (isBlockEnd) {
                                    Image(
                                        painter = rememberAsyncImagePainter(state.sender?.photo),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(recieverImageSizeInAnswer.dp)
                                            .clip(RoundedCornerShape(100.dp))
                                            .align(Alignment.Bottom),
                                        contentScale = ContentScale.Crop
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                }
                                else {
                                    Spacer(modifier = Modifier.width(recieverImageSizeInAnswer.dp + 6.dp))
                                }
                                Box(
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .clip(shape)
                                        .background(White.copy(0.5f))
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                        .padding(start = 6.dp)
                                ) {
                                    Text(
                                        text = msg.message,
                                        style = Inter17,
                                        color = White,
                                        letterSpacing = (-0.41).sp
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))


                    if (msg.isTimeVisible) {
                        Log.d("yoxla1", "ChatScreen: ${msg.timestamp.toString()} ")
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = msg.timestamp?.toReadableTime() ?: "1",
                                style = Inter13,
                                color = White.copy(0.7f)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            LaunchedEffect(state.messages.size) {
                if (state.messages.isNotEmpty()) {
                    listState.animateScrollToItem(state.messages.size - 1)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    ChatScreen("1", onBackCLick = {}, state = ChatState(), onEvent = {})
}
