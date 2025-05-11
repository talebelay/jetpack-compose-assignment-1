package com.example.courselistapp

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.courselistapp.ui.theme.CourseListAppTheme
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore


data class Course(
    val title: String,
    val code: String,
    val creditHours: Int,
    val description: String,
    val prerequisites: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Sample course data is defined at the activity level with 10 courses
        val courseData = listOf(
            Course(
                title = "Introduction to Computer Science",
                code = "CS101",
                creditHours = 3,
                description = "Learn the basics of computing, programming, and data structures.",
                prerequisites = "None"
            ),
            Course(
                title = "Data Structures",
                code = "CS102",
                creditHours = 4,
                description = "Study of arrays, linked lists, stacks, queues, trees, and graphs.",
                prerequisites = "CS101"
            ),
            Course(
                title = "Operating Systems",
                code = "CS201",
                creditHours = 3,
                description = "Explore how OS manage processes, memory, and hardware.",
                prerequisites = "CS102"
            ),
            Course(
                title = "Database Systems",
                code = "CS202",
                creditHours = 3,
                description = "Understand relational databases, SQL, and transactions.",
                prerequisites = "CS101"
            ),
            Course(
                title = "Mobile App Development",
                code = "CS301",
                creditHours = 4,
                description = "Learn to build native Android applications using Kotlin and Jetpack Compose.",
                prerequisites = "CS102, CS202"
            ),
            Course(
                title = "Artificial Intelligence",
                code = "CS401",
                creditHours = 3,
                description = "Introduction to AI concepts, machine learning, and neural networks.",
                prerequisites = "CS201, MATH301"
            ),
            Course(
                title = "Web Development",
                code = "CS302",
                creditHours = 3,
                description = "Learn to build responsive web applications using modern frameworks.",
                prerequisites = "CS102"
            ),
            Course(
                title = "Computer Networks",
                code = "CS303",
                creditHours = 3,
                description = "Study of network protocols, architecture, and security principles.",
                prerequisites = "CS201"
            ),
            Course(
                title = "Software Engineering",
                code = "CS304",
                creditHours = 4,
                description = "Learn software development methodologies, testing, and project management.",
                prerequisites = "CS102, CS202"
            ),
            Course(
                title = "Computer Graphics",
                code = "CS402",
                creditHours = 3,
                description = "Study of 2D and 3D rendering techniques and visualization.",
                prerequisites = "CS102, MATH202"
            )
        )

        setContent {
            CourseListAppTheme {
                CourseListApp(courses = courseData)
            }
        }
    }
}

@Composable
fun CourseListApp(
    courses: List<Course>,
    modifier: Modifier = Modifier
) {
    // State hoisting: The courses are now passed as a parameter
    var showOnboarding by rememberSaveable { mutableStateOf(true) }

    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        if (showOnboarding) {
            OnboardingScreen(onContinue = { showOnboarding = false })
        } else {
            CourseListScreen(courses = courses)
        }
    }
}

@Composable
fun OnboardingScreen(onContinue: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to Course List App!", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onContinue) {
            Text("Continue")
        }
    }
}

@Composable
fun CourseListScreen(courses: List<Course>, modifier: Modifier = Modifier) {
    // The course list is now received as a parameter (state hoisting)
    LazyColumn(modifier = modifier.padding(8.dp)) {
        items(courses) { course ->
            // Each item manages its own expanded state using rememberSaveable
            CourseCard(course = course)
        }
    }
}

@Composable
fun CourseCard(
    course: Course,
    modifier: Modifier = Modifier
) {
    // Each card manages its own expanded state using rememberSaveable
    // This ensures state is preserved during recomposition and configuration changes
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { isExpanded = !isExpanded }
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = course.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        Text(
                            text = "Code: ${course.code}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Credit Hours: ${course.creditHours}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (isExpanded) "Collapse" else "Expand"
                    )
                }
            }

            // Display expanded content if isExpanded is true
            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Description: ${course.description}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Prerequisites: ${course.prerequisites}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

// Light mode preview
@Preview(
    showBackground = true,
    name = "Light Mode"
)
@Composable
fun CourseListAppLightPreview() {
    CourseListAppTheme {
        CourseListApp(courses = listOf(
            Course(
                title = "Introduction to Computer Science",
                code = "CS101",
                creditHours = 3,
                description = "Learn the basics of computing, programming, and data structures.",
                prerequisites = "None"
            ),
            Course(
                title = "Data Structures",
                code = "CS102",
                creditHours = 4,
                description = "Study of arrays, linked lists, stacks, queues, trees, and graphs.",
                prerequisites = "CS101"
            ),
            Course(
                title = "Operating Systems",
                code = "CS201",
                creditHours = 3,
                description = "Explore how OS manage processes, memory, and hardware.",
                prerequisites = "CS102"
            )
        ))
    }
}

// Dark mode preview
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark Mode"
)
@Composable
fun CourseListAppDarkPreview() {
    CourseListAppTheme {
        CourseListApp(courses = listOf(
            Course(
                title = "Introduction to Computer Science",
                code = "CS101",
                creditHours = 3,
                description = "Learn the basics of computing, programming, and data structures.",
                prerequisites = "None"
            ),
            Course(
                title = "Data Structures",
                code = "CS102",
                creditHours = 4,
                description = "Study of arrays, linked lists, stacks, queues, trees, and graphs.",
                prerequisites = "CS101"
            ),
            Course(
                title = "Operating Systems",
                code = "CS201",
                creditHours = 3,
                description = "Explore how OS manage processes, memory, and hardware.",
                prerequisites = "CS102"
            )
        ))
    }
}