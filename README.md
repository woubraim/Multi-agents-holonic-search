# Holonic File Search System in SARL

This project implements a multi-agent system in SARL that dynamically searches for files across a hierarchical directory structure. Each agent works autonomously within a holonic framework, allowing agents to spawn sub-agents as needed to explore subdirectories. The project exemplifies efficient multi-agent coordination in large and complex file systems.
Project Goal

The purpose of this system is to build a holonic multi-agent file search solution. Each agent is assigned a directory to search, matching files based on specified criteria (e.g., file extension .sarl). Agents autonomously spawn sub-agents for deeper directory layers, enabling a scalable and efficient file search across complex directory hierarchies.
System Components
Core Agents

  BootAgent (boot.sarl)
  
        Role: Initializes the system by spawning both the SearchManager and an initial SearchAgent.
        Functionality: Spawns the primary agents (SearchManager and SearchAgent), then terminates itself.

  SearchManager (searchmanager.sarl)
  
        Role: Manages the overall search process and coordinates between the agents.
        Functionality:
        
            Sets the initial search path and criteria (.sarl files).
            Sends the initial SearchRequest event to start the search process.
            Receives FileFound events from agents when files matching the criteria are located.
            Waits for SearchFinished events to know when the search is complete, then shuts down after a delay.

  SearchAgent (search.sarl)
  
        Role: Conducts the search within an assigned directory, and spawns sub-agents for any detected subdirectories.
        Functionality:
        
            Searches the directory specified in the SearchRequest.
            Checks each item in the directory: if it’s a subdirectory, spawns a new SearchAgent to handle it; if it’s a file matching the search criteria, sends a FileFound event to the SearchManager.
            Sends a SearchFinished event to indicate when its assigned search is complete.

Events

  SearchRequest (events.sarl)
    
        Attributes:
            dirPath: The directory path for the search.
            searchCriteria: File criteria (e.g., extension .sarl) to match files.
        Purpose: Initiates a search in a specified directory.

  FileFound (events.sarl)
  
        Attributes:
            fileFoundPath: The path of a file that meets the search criteria.
        Purpose: Notifies the SearchManager of files that match the search criteria.

  SearchFinished (events.sarl)
  
        Attributes: None
        Purpose: Indicates that the search in a specific directory (or by a specific agent) is complete.

How It Works

    Initialization:
        BootAgent spawns the SearchManager and a primary SearchAgent.

    Search Process:
        SearchManager sends a SearchRequest with the target directory and search criteria.
        The primary SearchAgent searches the directory:
            Matches files based on criteria (e.g., .sarl extension) and reports them to SearchManager.
            Detects subdirectories and spawns new SearchAgents for each subdirectory.
        Each sub-agent repeats the search process in its directory, following the same rules.

    Event Handling:
        FileFound: SearchAgents emit this event when a file meets the criteria.
        SearchFinished: Emitted by SearchAgents upon completion. SearchManager uses these events to track search completion and shuts down after all agents finish.

Project Structure

    boot.sarl: Contains BootAgent, responsible for initializing the system.
    searchmanager.sarl: Defines the SearchManager agent, coordinating the file search and aggregating results.
    search.sarl: Contains the SearchAgent logic for directory searches and recursive agent spawning.
    events.sarl: Defines events used for inter-agent communication.

Requirements

    SARL: This project is developed in SARL, so SARL must be installed and configured in your environment.
    Janus Platform: The Janus platform is required for SARL projects; configure it accordingly.

Running the Project

    Compile and run the SARL project in a compatible IDE.
    Observe the agent activity and search results in the console.
    Results from matching files will be logged, and the system shuts down when the search completes.

Author

This project was developed as part of a coursework exercise on multi-agent systems, demonstrating holonic organization in a file search application.
