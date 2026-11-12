package com.example.data

import com.example.model.SemesterInfo
import com.example.model.Subject
import com.example.model.UnitTopic

object SyllabusData {

  val semesters: List<SemesterInfo> = listOf(
    SemesterInfo(
      semesterNumber = 1,
      title = "1st Semester",
      isContentReady = false,
      notice = "Syllabus content will be added soon."
    ),
    SemesterInfo(
      semesterNumber = 2,
      title = "2nd Semester",
      isContentReady = false,
      notice = "Syllabus content will be added soon."
    ),
    SemesterInfo(
      semesterNumber = 3,
      title = "3rd Semester",
      isContentReady = true
    ),
    SemesterInfo(
      semesterNumber = 4,
      title = "4th Semester",
      isContentReady = true
    ),
    SemesterInfo(
      semesterNumber = 5,
      title = "5th Semester",
      isContentReady = true
    ),
    SemesterInfo(
      semesterNumber = 6,
      title = "6th Semester",
      isContentReady = true
    )
  )

  val allSubjects: List<Subject> = listOf(
    // 3rd Semester Theory
    Subject(
      code = "AI-301",
      name = "Mathematics-II",
      semester = 3,
      isPractical = false,
      description = "Advanced engineering mathematics including differential equations, Laplace transforms, and numerical methods.",
      units = listOf(
        UnitTopic(1, "Differential Equations of First Order", listOf("Formation of differential equations", "Variables separable method", "Homogeneous differential equations", "Linear differential equations", "Exact differential equations")),
        UnitTopic(2, "Higher Order Linear Differential Equations", listOf("Linear equations with constant coefficients", "Particular integral methods", "Homogeneous linear differential equations", "Simultaneous linear differential equations")),
        UnitTopic(3, "Laplace Transforms", listOf("Definition and properties", "Transforms of elementary functions", "First and second shifting theorems", "Periodic functions", "Inverse Laplace transforms")),
        UnitTopic(4, "Applications of Laplace Transforms", listOf("Convolution theorem", "Solving differential equations using Laplace transforms", "Unit step function", "Dirac delta function")),
        UnitTopic(5, "Fourier Series", listOf("Periodic functions", "Dirichlet conditions", "Fourier series in [-π, π] and [0, 2π]", "Half-range sine and cosine series"))
      )
    ),
    Subject(
      code = "AI-302",
      name = "Java Programming",
      semester = 3,
      isPractical = false,
      description = "Core Java language, object-oriented principles, multithreading, collections, and event-driven architecture.",
      units = listOf(
        UnitTopic(1, "Java Fundamentals & OOP", listOf("Java Virtual Machine (JVM)", "Bytecode & JDK", "Data types, operators, and control structures", "Classes, objects, and constructors", "Method overloading and 'this' keyword")),
        UnitTopic(2, "Inheritance, Interfaces & Packages", listOf("Types of inheritance", "Method overriding and 'super' keyword", "Abstract classes vs Interfaces", "Defining and importing packages", "Access specifiers")),
        UnitTopic(3, "Exception Handling & Multithreading", listOf("Try, catch, finally blocks", "Throw and throws keywords", "Custom user-defined exceptions", "Thread lifecycle and creation", "Synchronization and inter-thread communication")),
        UnitTopic(4, "Java I/O & File Streams", listOf("Byte streams and Character streams", "FileInputStream and FileOutputStream", "BufferedReader and BufferedWriter", "Serialization and Deserialization")),
        UnitTopic(5, "Java Collections Framework", listOf("List, ArrayList, and LinkedList", "Set and HashSet", "Map and HashMap", "Iterator and ListIterator", "Generics in Java"))
      )
    ),
    Subject(
      code = "AI-303",
      name = "Operating Systems",
      semester = 3,
      isPractical = false,
      description = "Fundamental OS concepts, process management, scheduling, concurrency, memory management, and file systems.",
      units = listOf(
        UnitTopic(1, "Introduction to Operating Systems", listOf("Evolution and types of OS", "System calls and OS structure", "Kernel architecture", "Process control blocks (PCB)", "Process states and transitions")),
        UnitTopic(2, "CPU Scheduling & Concurrency", listOf("Scheduling criteria", "FCFS, SJF, Priority, Round Robin", "Critical section problem", "Semaphores and mutex locks", "Classic synchronization problems")),
        UnitTopic(3, "Deadlocks", listOf("Deadlock characterization", "Resource allocation graph", "Deadlock prevention and avoidance", "Banker's Algorithm", "Deadlock detection and recovery")),
        UnitTopic(4, "Memory Management", listOf("Contiguous memory allocation", "Paging and Segmentation", "Virtual memory and Demand paging", "Page replacement algorithms (FIFO, LRU, Optimal)")),
        UnitTopic(5, "File & Storage Management", listOf("File concepts and access methods", "Directory structures", "Disk scheduling algorithms (FCFS, SSTF, SCAN, C-SCAN)", "RAID structures"))
      )
    ),
    Subject(
      code = "AI-304",
      name = "Data Structures through C",
      semester = 3,
      isPractical = false,
      description = "Analysis of algorithms, linear and non-linear data structures, trees, graphs, and hashing techniques.",
      units = listOf(
        UnitTopic(1, "Introduction & Stacks", listOf("Algorithm complexity (Big-O)", "Arrays and sparse matrices", "Stack ADT and array implementation", "Infix to postfix conversion", "Evaluation of postfix expressions")),
        UnitTopic(2, "Queues", listOf("Queue ADT and array representation", "Circular queues", "Double-ended queues (Deque)", "Priority queues", "Applications of queues")),
        UnitTopic(3, "Linked Lists", listOf("Singly linked list operations", "Doubly linked list", "Circular linked list", "Linked implementation of stack and queue", "Polynomial representation and addition")),
        UnitTopic(4, "Trees & Binary Search Trees", listOf("Tree terminology", "Binary tree representations", "Tree traversals (Inorder, Preorder, Postorder)", "Binary Search Tree (BST) operations", "AVL tree rotations overview")),
        UnitTopic(5, "Graphs, Searching & Sorting", listOf("Graph representations (Matrix & List)", "BFS and DFS traversals", "Linear and Binary search", "Bubble, Selection, Insertion, Quick, and Merge sort", "Hashing concepts"))
      )
    ),
    Subject(
      code = "AI-305",
      name = "Artificial Intelligence",
      semester = 3,
      isPractical = false,
      description = "Core foundations of AI, state space search, heuristic algorithms, knowledge representation, and logic.",
      units = listOf(
        UnitTopic(1, "Introduction to AI & Intelligent Agents", listOf("Definition and history of AI", "Turing test", "Agent architectures", "PEAS description", "Environment types")),
        UnitTopic(2, "Uninformed & Informed Search", listOf("State space representation", "Breadth First Search (BFS)", "Depth First Search (DFS)", "Heuristic search strategies", "A* search and Greedy Best-First Search")),
        UnitTopic(3, "Game Playing & Constraint Satisfaction", listOf("Minimax algorithm", "Alpha-Beta pruning", "Constraint Satisfaction Problems (CSP)", "Backtracking for CSP", "Cryptarithmetic puzzles")),
        UnitTopic(4, "Knowledge Representation & Logic", listOf("Propositional logic", "First-Order Predicate Logic (FOPL)", "Inference and resolution", "Semantic networks and Frames", "Conceptual dependency")),
        UnitTopic(5, "Uncertain Knowledge & Expert Systems", listOf("Probabilistic reasoning", "Bayes' Rule and Bayesian networks", "Architecture of Expert Systems", "Inference engines and forward/backward chaining"))
      )
    ),
    // 3rd Semester Practicals
    Subject(
      code = "AI-306",
      name = "Java Programming Lab",
      semester = 3,
      isPractical = true,
      description = "Hands-on implementation of OOP concepts, classes, inheritance, exception handling, multithreading, and collections in Java.",
      units = listOf(
        UnitTopic(1, "Core Java & Classes Lab", listOf("Basic syntax, arrays, command-line arguments", "Class, constructors, method overloading")),
        UnitTopic(2, "Inheritance & Exception Lab", listOf("Inheritance hierarchies and interface implementation", "User-defined exception handlers")),
        UnitTopic(3, "Threads & Collections Lab", listOf("Producer-consumer problem using threads", "CRUD operations with ArrayList and HashMap"))
      )
    ),
    Subject(
      code = "AI-307",
      name = "Data Structures through C Lab",
      semester = 3,
      isPractical = true,
      description = "Implementation of Stacks, Queues, Linked Lists, Binary Trees, Search algorithms, and Sorting techniques in C.",
      units = listOf(
        UnitTopic(1, "Stacks & Queues in C", listOf("Array implementation of stack", "Infix to postfix evaluator", "Circular queue implementation")),
        UnitTopic(2, "Linked Lists & Trees", listOf("Singly linked list operations", "Binary search tree construction and traversal")),
        UnitTopic(3, "Sorting & Searching", listOf("Binary search implementation", "Quick sort and Merge sort programs"))
      )
    ),
    Subject(
      code = "AI-308",
      name = "Computer Networking & Cyber Security Lab",
      semester = 3,
      isPractical = true,
      description = "Network configuration, packet analysis using Wireshark, IP addressing, socket programming, and security principles.",
      units = listOf(
        UnitTopic(1, "Network Configuration", listOf("RJ-45 crimping and Ethernet cabling", "IP address configuration and ping test", "Router and switch setup")),
        UnitTopic(2, "Socket Programming & Tools", listOf("Client-Server TCP socket program", "Packet capture using Wireshark", "Port scanning with Nmap")),
        UnitTopic(3, "Cyber Security Fundamentals", listOf("Symmetric encryption implementation", "Firewall configuration and basic penetration testing"))
      )
    ),
    Subject(
      code = "AI-309",
      name = "Artificial Intelligence Lab using PROLOG",
      semester = 3,
      isPractical = true,
      description = "Logic programming with PROLOG, facts, rules, queries, recursion, list manipulation, and AI problem solving.",
      units = listOf(
        UnitTopic(1, "PROLOG Basics", listOf("Facts, rules, and queries", "Syntax, variables, and unification")),
        UnitTopic(2, "Logic & Recursion", listOf("Family tree relation program", "Factorial and Fibonacci recursion in PROLOG")),
        UnitTopic(3, "AI Search in PROLOG", listOf("8-puzzle problem representation", "Water jug problem using PROLOG rules"))
      )
    ),

    // 4th Semester Theory
    Subject(
      code = "AI-401",
      name = "Software Engineering",
      semester = 4,
      isPractical = false,
      description = "Software lifecycles, Agile methodology, requirements engineering, UML modeling, software testing, and maintenance.",
      units = listOf(
        UnitTopic(1, "Software Process Models", listOf("Waterfall model, V-model, Evolutionary models", "Agile methodologies (Scrum, Kanban, Extreme Programming)", "Software engineering ethics")),
        UnitTopic(2, "Requirement Engineering", listOf("Functional and Non-functional requirements", "SRS document preparation", "Use Case modeling", "Requirement validation techniques")),
        UnitTopic(3, "Software Design & Architecture", listOf("Cohesion and Coupling principles", "Architectural styles (Layered, Client-Server, Microservices)", "UML Class, Sequence, and Activity diagrams")),
        UnitTopic(4, "Software Testing", listOf("Unit testing, Integration testing, System testing", "White-box testing (Basis path, Cyclomatic complexity)", "Black-box testing (Equivalence partitioning, BVA)", "Automated testing overview")),
        UnitTopic(5, "Software Project Management & Quality", listOf("Cost estimation (COCOMO)", "Risk management strategies", "Quality standards (ISO 9001, CMMI)", "Software maintenance and reengineering"))
      )
    ),
    Subject(
      code = "AI-402",
      name = "Web Technologies",
      semester = 4,
      isPractical = false,
      description = "Full stack web fundamentals: HTML5, CSS3, JavaScript, DOM manipulation, responsive design, and server-side web scripting.",
      units = listOf(
        UnitTopic(1, "HTML5 & Semantic Markup", listOf("Structure of HTML documents", "Forms and input validation", "HTML5 audio/video and Canvas", "Semantic tags and accessibility")),
        UnitTopic(2, "CSS3 & Modern Styling", listOf("Box model, Flexbox, and CSS Grid", "Responsive Web Design with Media Queries", "Transitions, animations, and pseudo-classes")),
        UnitTopic(3, "JavaScript Fundamentals", listOf("Variables (let/const), data types, operators", "Functions, closures, and ES6 arrow functions", "DOM manipulation and Event listeners")),
        UnitTopic(4, "Asynchronous JS & APIs", listOf("Promises, Async/Await syntax", "Fetch API and JSON handling", "Local storage and Session storage", "AJAX concepts")),
        UnitTopic(5, "Server-side Concepts & Frameworks", listOf("Client-server HTTP requests", "RESTful API principles", "Introduction to Node.js/Express or PHP basics", "Web security fundamentals (CORS, XSS, CSRF)"))
      )
    ),
    Subject(
      code = "AI-403",
      name = "DBMS",
      semester = 4,
      isPractical = false,
      description = "Relational database concepts, ER modeling, SQL querying, normalization, indexing, transaction processing, and concurrency control.",
      units = listOf(
        UnitTopic(1, "Database Foundations & ER Model", listOf("Data models, schemas, and instances", "Three-schema architecture", "Entity-Relationship (ER) diagram components", "Extended ER features (Generalization, Specialization)")),
        UnitTopic(2, "Relational Model & Relational Algebra", listOf("Relational integrity constraints", "Relational algebra operations (Select, Project, Join, Set)", "Tuple and Domain relational calculus")),
        UnitTopic(3, "Structured Query Language (SQL)", listOf("DDL, DML, DCL, and TCL commands", "Aggregate functions and GROUP BY/HAVING", "Nested subqueries and Joins", "Views, Indexes, and Triggers")),
        UnitTopic(4, "Relational Database Design & Normalization", listOf("Functional dependencies and inference rules", "First, Second, and Third Normal Forms (1NF, 2NF, 3NF)", "Boyce-Codd Normal Form (BCNF)", "Lossless join and dependency preservation")),
        UnitTopic(5, "Transactions & Concurrency Control", listOf("ACID properties of transactions", "Serializability and schedules", "Two-Phase Locking (2PL) protocol", "Deadlock handling in DBMS", "Database recovery techniques (WAL, Checkpoints)"))
      )
    ),
    Subject(
      code = "AI-404",
      name = "Python Programming",
      semester = 4,
      isPractical = false,
      description = "Core Python syntax, data structures, functional programming, OOP, file handling, and scientific computing with NumPy & Pandas.",
      units = listOf(
        UnitTopic(1, "Python Basics & Control Flow", listOf("Python syntax, identifiers, dynamic typing", "Conditional statements and loops", "Lists, Tuples, Sets, and Dictionaries", "List comprehensions")),
        UnitTopic(2, "Functions & Modules", listOf("Function definitions, parameters (*args, **kwargs)", "Lambda functions, map, filter, reduce", "Creating and importing custom modules", "Standard library modules (math, random, sys)")),
        UnitTopic(3, "Object Oriented Python", listOf("Classes, attributes, and __init__ method", "Inheritance and polymorphism", "Dunder/Magic methods", "Encapsulation and property decorators")),
        UnitTopic(4, "Exception & File Handling", listOf("Try, except, else, finally blocks", "Custom exception classes", "Reading and writing text & CSV files", "Context managers with 'with' statement")),
        UnitTopic(5, "Data Analysis Libraries", listOf("NumPy arrays, slicing, and operations", "Pandas Series and DataFrame manipulation", "Data cleaning and filtering", "Matplotlib visualization basics"))
      )
    ),
    Subject(
      code = "AI-405",
      name = "Internet of Things",
      semester = 4,
      isPractical = false,
      description = "IoT architecture, sensors, actuators, microcontrollers, communication protocols (MQTT, CoAP), and cloud IoT integration.",
      units = listOf(
        UnitTopic(1, "Introduction to IoT", listOf("Definition, characteristics, and components of IoT", "Physical design and logical design", "IoT enabled technologies", "IoT levels and deployment templates")),
        UnitTopic(2, "Sensors, Actuators & Hardware", listOf("Sensor types (temperature, ultrasonic, PIR, IR)", "Actuators (relays, servos, motors)", "Arduino and Raspberry Pi hardware overview", "GPIO pin control")),
        UnitTopic(3, "IoT Communication Protocols", listOf("Network layer: 6LoWPAN, Zigbee, LoRaWAN", "Application layer: MQTT, CoAP, HTTP, WebSockets", "Comparison of MQTT vs HTTP")),
        UnitTopic(4, "Cloud Platforms for IoT", listOf("IoT Cloud architecture", "ThingSpeak, AWS IoT, and Blynk", "Publishing telemetry data to cloud", "Cloud analytics and dashboard creation")),
        UnitTopic(5, "IoT Applications & Security", listOf("Smart Home, Smart Agriculture, Smart Cities", "Industrial IoT (IIoT)", "IoT security challenges and device hardening"))
      )
    ),
    // 4th Semester Practicals
    Subject(
      code = "AI-406",
      name = "Web Technologies Lab",
      semester = 4,
      isPractical = true,
      description = "Designing responsive web applications using HTML5, CSS3, JavaScript DOM manipulation, and dynamic API requests.",
      units = listOf(
        UnitTopic(1, "Responsive Web Layouts", listOf("Building semantic student portal using HTML5/CSS3", "Creating multi-device Flexbox and Grid layouts")),
        UnitTopic(2, "Interactive JavaScript", listOf("Form validation script with regex", "To-Do task manager using DOM and localStorage")),
        UnitTopic(3, "API Integration", listOf("Fetching live weather/student data with async Fetch API"))
      )
    ),
    Subject(
      code = "AI-407",
      name = "DBMS Lab",
      semester = 4,
      isPractical = true,
      description = "Hands-on SQL practice: table creation, complex joins, subqueries, triggers, stored procedures, and database normalization exercises.",
      units = listOf(
        UnitTopic(1, "SQL DDL & DML Commands", listOf("Creating student database schema with foreign keys", "Inserting, updating, and querying records")),
        UnitTopic(2, "Joins, Views & Subqueries", listOf("Writing Inner, Left, and Full Outer joins", "Creating secure database views")),
        UnitTopic(3, "PL/SQL Procedures & Triggers", listOf("Writing stored procedure for mark calculations", "Creating automated audit triggers on table updates"))
      )
    ),
    Subject(
      code = "AI-408",
      name = "Communication Skills",
      semester = 4,
      isPractical = true,
      description = "Technical presentation, interview skills, group discussions, business correspondence, and workplace communication.",
      units = listOf(
        UnitTopic(1, "Oral Presentations", listOf("Technical seminar presentation delivery", "Body language and voice modulation")),
        UnitTopic(2, "Group Discussions & Interviews", listOf("Participating constructively in GDs", "Mock HR and technical interview prep")),
        UnitTopic(3, "Professional Writing", listOf("Resume and cover letter preparation", "Professional email etiquette and report writing"))
      )
    ),
    Subject(
      code = "AI-409",
      name = "Python Programming Lab",
      semester = 4,
      isPractical = true,
      description = "Writing Python programs for data structures, OOP, file processing, and data manipulation with NumPy and Pandas.",
      units = listOf(
        UnitTopic(1, "Python Core Programs", listOf("Palindrome, prime generator, fibonacci series", "Dictionary word-frequency counter")),
        UnitTopic(2, "OOP & File I/O", listOf("Student gradebook class with inheritance", "CSV data processor and report generator")),
        UnitTopic(3, "NumPy & Pandas Practice", listOf("Matrix multiplication with NumPy", "Dataset filtering and statistical aggregation with Pandas"))
      )
    ),

    // 5th Semester Theory
    Subject(
      code = "AI-501",
      name = "Industrial Management and Entrepreneurship",
      semester = 5,
      isPractical = false,
      description = "Principles of management, organizational structure, entrepreneurship, project appraisal, finance, and quality systems.",
      units = listOf(
        UnitTopic(1, "Principles of Management", listOf("Functions of management (Planning, Organizing, Leading, Controlling)", "Scientific management principles (Taylor and Fayol)", "Types of organizational structures")),
        UnitTopic(2, "Production & Materials Management", listOf("Plant layout and location factors", "Inventory control models (EOQ, ABC analysis)", "Supply chain management fundamentals")),
        UnitTopic(3, "Financial & Marketing Management", listOf("Sources of industrial finance", "Costing and Break-Even Analysis (BEA)", "Marketing mix (4 Ps)", "Market survey techniques")),
        UnitTopic(4, "Entrepreneurship Development", listOf("Role of entrepreneurs in economic development", "Identification of business opportunities", "Business plan and Project Report preparation", "Government support schemes (MSME, Startup India)")),
        UnitTopic(5, "Total Quality Management & Industrial Laws", listOf("TQM concepts and ISO 9000 standards", "Six Sigma methodology", "Factories Act, Industrial Disputes Act, and safety norms"))
      )
    ),
    Subject(
      code = "AI-502",
      name = "Computer Vision",
      semester = 5,
      isPractical = false,
      description = "Image processing fundamentals, spatial filters, edge detection, feature extraction, object detection, and OpenCV techniques.",
      units = listOf(
        UnitTopic(1, "Digital Image Fundamentals", listOf("Image sampling and quantization", "Color spaces (RGB, HSV, Grayscale)", "Pixel relationships and basic transformations")),
        UnitTopic(2, "Image Enhancement & Filtering", listOf("Histogram equalization", "Spatial filtering (Smoothing, Sharpening, Gaussian filter)", "Frequency domain filtering overview")),
        UnitTopic(3, "Edge Detection & Segmentation", listOf("Gradient operators (Sobel, Prewitt, Robert)", "Canny edge detector algorithm", "Thresholding methods (Otsu's method)", "Hough transform for lines and circles")),
        UnitTopic(4, "Feature Extraction & Description", listOf("Corner detection (Harris Corner)", "SIFT and SURF features overview", "ORB feature descriptor", "Blob detection")),
        UnitTopic(5, "Object Detection & Recognition", listOf("Template matching", "Haar cascades for face detection", "Deep learning in vision (CNN overview)", "OpenCV pipeline for vision systems"))
      )
    ),
    Subject(
      code = "AI-503",
      name = "Natural Language Processing",
      semester = 5,
      isPractical = false,
      description = "Text preprocessing, N-grams, part-of-speech tagging, syntax parsing, sentiment analysis, word embeddings, and transformers.",
      units = listOf(
        UnitTopic(1, "Introduction to NLP & Text Preprocessing", listOf("NLP pipeline overview", "Tokenization, Lemmatization, and Stemming", "Stopword removal and Regex in text processing")),
        UnitTopic(2, "Language Modeling & POS Tagging", listOf("N-gram language models", "Perplexity metric", "Part-of-Speech (POS) tagging with HMM", "Viterbi algorithm")),
        UnitTopic(3, "Syntactic & Semantic Analysis", listOf("Context-Free Grammars (CFG)", "Constituency and Dependency parsing", "Named Entity Recognition (NER)", "Word sense disambiguation")),
        UnitTopic(4, "Word Representations & Embeddings", listOf("Bag of Words (BoW) and TF-IDF", "Word2Vec (CBOW and Skip-gram)", "GloVe embeddings", "Cosine similarity for documents")),
        UnitTopic(5, "Modern NLP & Transformers", listOf("Sequence-to-Sequence models", "Attention mechanism overview", "Transformers and BERT architecture introduction", "Sentiment analysis application"))
      )
    ),
    Subject(
      code = "AI-504",
      name = "Artificial Neural Networks and Deep Learning",
      semester = 5,
      isPractical = false,
      description = "Perceptrons, multi-layer networks, backpropagation, CNNs, RNNs, optimization techniques, and deep learning architectures.",
      units = listOf(
        UnitTopic(1, "Biological to Artificial Neurons", listOf("Biological neuron analogy", "McCulloch-Pitts neuron", "Perceptron model and learning rule", "Perceptron limitations (XOR problem)")),
        UnitTopic(2, "Multilayer Perceptron & Backpropagation", listOf("Multilayer Feedforward Networks", "Activation functions (Sigmoid, Tanh, ReLU, Softmax)", "Backpropagation algorithm derivation", "Gradient descent and Adam optimizers")),
        UnitTopic(3, "Training Deep Networks", listOf("Vanishing and Exploding gradients", "Batch normalization", "Overfitting prevention (Dropout, L1/L2 regularization)", "Early stopping")),
        UnitTopic(4, "Convolutional Neural Networks (CNN)", listOf("Convolution operation and kernels", "Pooling layers (Max, Average)", "CNN architectures (LeNet, AlexNet, VGG)", "Image classification with CNNs")),
        UnitTopic(5, "Recurrent Neural Networks (RNN)", listOf("Sequential modeling", "Standard RNN architecture", "Long Short-Term Memory (LSTM) cells", "Gated Recurrent Units (GRU)"))
      )
    ),
    Subject(
      code = "AI-505",
      name = "Introduction to Machine Learning",
      semester = 5,
      isPractical = false,
      description = "Supervised and unsupervised learning, linear regression, logistic regression, decision trees, SVM, clustering, and model evaluation.",
      units = listOf(
        UnitTopic(1, "Machine Learning Foundations", listOf("Types of ML (Supervised, Unsupervised, Reinforcement)", "Training, validation, and test datasets", "Bias-Variance tradeoff", "Cross-validation techniques")),
        UnitTopic(2, "Supervised Regression", listOf("Simple and Multiple Linear Regression", "Cost function and Ordinary Least Squares", "Polynomial regression", "Regularization (Ridge and Lasso)")),
        UnitTopic(3, "Supervised Classification", listOf("Logistic regression for binary classification", "K-Nearest Neighbors (KNN)", "Decision Trees (ID3, CART, Gini index)", "Support Vector Machines (SVM) and Kernels")),
        UnitTopic(4, "Unsupervised Learning", listOf("Clustering concepts", "K-Means clustering algorithm", "Hierarchical clustering", "Principal Component Analysis (PCA) for dimensionality reduction")),
        UnitTopic(5, "Model Evaluation & Ensemble Methods", listOf("Confusion matrix, Accuracy, Precision, Recall, F1-Score", "ROC curve and AUC", "Ensemble learning: Random Forest and Gradient Boosting"))
      )
    ),
    // 5th Semester Practicals
    Subject(
      code = "AI-506",
      name = "NLP Lab using Python",
      semester = 5,
      isPractical = true,
      description = "Implementation of text processing, sentiment analysis, NLTK/spaCy tokenization, POS tagging, and TF-IDF models in Python.",
      units = listOf(
        UnitTopic(1, "Text Preprocessing Lab", listOf("Tokenization and lemmatization using NLTK", "Building vocabulary and Stopword removal")),
        UnitTopic(2, "POS & Entity Extraction", listOf("Parts-of-speech tagging with NLTK/spaCy", "Named Entity Recognition on technical news")),
        UnitTopic(3, "Text Classification", listOf("Spam SMS detection using TF-IDF and Naive Bayes", "Twitter sentiment analyzer program"))
      )
    ),
    Subject(
      code = "AI-507",
      name = "Machine Learning Lab",
      semester = 5,
      isPractical = true,
      description = "Developing machine learning models using Scikit-Learn: regression, decision trees, KNN, SVM, K-Means clustering, and evaluation.",
      units = listOf(
        UnitTopic(1, "Regression Modeling", listOf("Linear regression on housing prices dataset", "Model evaluation with RMSE and R2-score")),
        UnitTopic(2, "Classification Algorithms", listOf("Iris dataset classification using KNN & Decision Trees", "Heart disease prediction with Logistic Regression")),
        UnitTopic(3, "Clustering & PCA", listOf("Customer segmentation using K-Means", "Dimensionality reduction using PCA"))
      )
    ),
    Subject(
      code = "AI-508",
      name = "Life Skills",
      semester = 5,
      isPractical = true,
      description = "Interpersonal effectiveness, emotional intelligence, stress management, critical thinking, and ethics in professional life.",
      units = listOf(
        UnitTopic(1, "Self Awareness & Emotional Intelligence", listOf("Understanding self-concept and strengths", "Empathy and interpersonal relationships")),
        UnitTopic(2, "Problem Solving & Critical Thinking", listOf("Creative problem-solving exercises", "Decision making under pressure")),
        UnitTopic(3, "Workplace Harmony & Ethics", listOf("Conflict resolution techniques", "Professional ethics and team dynamics"))
      )
    ),
    Subject(
      code = "AI-509",
      name = "Project Work",
      semester = 5,
      isPractical = true,
      description = "Capstone project selection, problem formulation, literature survey, system design, and AI prototype development.",
      units = listOf(
        UnitTopic(1, "Project Formulation", listOf("Problem statement definition and feasibility study", "Literature survey and technology selection")),
        UnitTopic(2, "System Design & Prototyping", listOf("System architecture and database schema design", "Module-wise implementation and testing")),
        UnitTopic(3, "Documentation & Presentation", listOf("Project report preparation as per standards", "Demonstration and viva voce defense"))
      )
    ),

    // 6th Semester
    Subject(
      code = "AI-601",
      name = "Industrial Training",
      semester = 6,
      isPractical = true,
      description = "Practical industrial internship training in software, artificial intelligence, data science, or engineering organizations.",
      units = listOf(
        UnitTopic(1, "Industrial Exposure & Onboarding", listOf("Industry orientation and corporate workplace culture", "Technical toolchains and production environments")),
        UnitTopic(2, "Live Project Execution", listOf("Working on assigned industrial project modules", "Sprint planning, agile reviews, and issue tracking")),
        UnitTopic(3, "Industrial Training Report & Viva", listOf("Comprehensive industrial training report preparation", "Internship logbook maintenance", "Technical presentation and viva voce"))
      )
    )
  )

  fun getSubjectsForSemester(semester: Int): List<Subject> {
    return allSubjects.filter { it.semester == semester }
  }

  fun getSubjectByCode(code: String): Subject? {
    return allSubjects.find { it.code.equals(code, ignoreCase = true) }
  }
}
