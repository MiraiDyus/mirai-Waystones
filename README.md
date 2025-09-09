# ZeroWays - Minecraft Waystone Plugin

A Minecraft Spigot/Paper plugin that transforms Lodestone blocks into interactive Waystones, enabling players to create a network of teleportation points across their world.

## Overview

ZeroWays enhances the vanilla Minecraft experience by repurposing Lodestone blocks as waystone teleportation hubs. Players can name these waystones and build a personal network of discovered locations for quick travel across their world.

## Features

### Core Functionality
- **Waystone Creation**: Place and name Lodestone blocks to create personal waystones
- **Discovery System**: Right-click any named waystone to add it to your personal collection
- **Interactive Teleportation**: Access an intuitive book-based menu to teleport between discovered waystones
- **Secure Transport**: Token-based system prevents unauthorized teleportation
- **Persistent Data**: All waystone locations and player discoveries are automatically saved

### Advanced Features
- **Dynamic Commands**: Configurable command shortcuts through the `/zw` system
- **Multi-dimensional Support**: Works across Overworld, Nether, and End dimensions
- **Collision Prevention**: Automatic name conflict resolution for duplicate waystone names
- **Real-time Validation**: Ensures waystone blocks exist before allowing teleportation

## Installation

### Requirements
- Minecraft Server: Spigot or Paper 1.21+
- Java: Version 21 or higher

### Setup Instructions
1. Download the latest `ZeroWays.jar` from the releases page
2. Place the JAR file in your server's `plugins/` directory
3. Restart your server
4. The plugin will automatically create necessary configuration files

## Configuration

### Main Configuration (`config.yml`)
The plugin supports dynamic command configuration through `config.yml`:

```yaml
commands:
  # Usage: /zw teleport.spawn
  teleport:
    spawn: "minecraft:teleport %player% 12 66 -3"
  
  give:
    diamond: "minecraft:give %player% minecraft:diamond 5"
  
  effect:
    speed: "effect give %player% minecraft:speed 30 1"
```

**Available Placeholders:**
- `%player%` - The name of the player executing the command

### Waystone Data (`lodestones.yml`)
Automatically managed file storing all named waystone locations. Manual editing is not recommended.

## Usage Guide

### For Players

#### Creating a Waystone
1. Place a Lodestone block in your desired location
2. An anvil GUI will automatically appear
3. Enter a unique name for your waystone (spaces will be automatically removed)
4. Confirm the name to create your waystone

#### Discovering Waystones
1. Right-click any named Lodestone block
2. The waystone will be automatically added to your personal collection
3. You'll receive a confirmation message

#### Teleporting Between Waystones
1. Right-click any discovered waystone
2. An interactive book menu will open showing all available destinations
3. Click on any waystone name to teleport instantly
4. The current waystone will be excluded from the menu

### For Administrators

#### Commands
- `/zw <shortcut>` - Execute configured command shortcuts
- `/zw home.<name>` - Internal teleportation command (player-accessible through waystone menu)

#### Permissions
The plugin uses console-level execution for teleportation, requiring no special permissions for basic waystone functionality.

## Technical Details

### Data Storage
- **Player Data**: Stored in memory with periodic saves every 4 minutes
- **Waystone Locations**: Persistent storage in `lodestones.yml`
- **Security Tokens**: Temporary UUID-based tokens for secure teleportation

### Performance Considerations
- Automatic cleanup of invalid waystone references
- Efficient caching system for player waystone data
- Block-accurate location validation

### Dependencies
- **Spigot API**: 1.21.8-R0.1-SNAPSHOT
- **AnvilGUI**: 1.10.8-SNAPSHOT for naming interface

## Building from Source

### Prerequisites
- Java Development Kit 21+
- Apache Maven 3.6+

### Build Instructions
```bash
git clone https://github.com/MiraiDyus/mirai-Waystones.git
cd mirai-Waystones
mvn clean package
```

The compiled JAR will be located in the `target/` directory.

### Development Setup
```bash
mvn clean compile
```

Note: External dependencies may require manual repository configuration for successful builds.

## Troubleshooting

### Common Issues

**Problem**: Waystone doesn't appear in teleportation menu
- **Solution**: Ensure the Lodestone block still exists at the registered location
- **Details**: The plugin automatically validates waystone existence and removes invalid entries

**Problem**: "Invalid or expired token" error
- **Solution**: Use the waystone interaction menu instead of manual commands
- **Details**: Security tokens are single-use and expire when the player disconnects

**Problem**: Cannot name waystone
- **Solution**: Ensure the name contains valid characters and isn't empty after space removal
- **Details**: Waystone names cannot consist entirely of spaces

**Problem**: Teleportation fails silently
- **Solution**: Check server console for error messages and verify target waystone exists
- **Details**: Teleportation uses console commands and requires valid target coordinates

### Data Recovery
- Waystone data is stored in `plugins/ZeroWays/lodestones.yml`
- Player discovery data is cached in memory and saved periodically
- Manual backup of the plugin directory is recommended for data protection

## Support and Contributing

### Reporting Issues
When reporting problems, please include:
- Server version and type (Spigot/Paper)
- Plugin version
- Console error messages
- Steps to reproduce the issue

### Development
This plugin is open-source and welcomes contributions. Please ensure all code follows existing patterns and includes appropriate error handling.

## License

This project is licensed under the terms specified in the LICENSE file.

## Credits

**Author**: angryzero  
**Project**: ZeroWays (mirai-Waystones)  
**Version**: 1.0-SNAPSHOT
