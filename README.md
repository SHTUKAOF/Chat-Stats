# Chat-Stats

A plugin for tracking player chat message statistics.

## Links

- 📥 [Download on Modrinth](https://modrinth.com/plugin/chat-stats)
- 💬 [Join Discord Community](https://discord.com/invite/RPq4y9hKY7)

## Features

- 📊 Track message count for each player
- 🏆 Top 10 players by chat activity
- 🌍 Support for Russian and English languages
- 🎨 Customizable interface color
- 🚫 Built-in anti-spam system
- 💾 Automatic statistics saving

## Installation

1. Download the latest plugin release
2. Place the JAR file in your server's `plugins` folder
3. Restart your server: `/reload`

## Commands

### For all players
- `/chatstats stats` - Show your message statistics
- `/chatstats top` - Show top 10 players
- `/chatstats help` - Plugin help

### For administrators (OP)
- `/chatstats stats <player>` - Show player statistics
- `/chatstats setlang <ru|en>` - Change plugin language
- `/chatstats setcolor <code>` - Change interface color
- `/chatstats antispam <on|off|cooldown|maxmsg>` - Manage anti-spam

## Color Codes

- `6` - Gold
- `e` - Yellow
- `a` - Green
- `b` - Cyan
- `d` - Purple
- `f` - White
- `c` - Red

## Configuration

Main config located at `plugins/ChatStats/config.yml`:

```yaml
language: ru  # Language: ru or en
bar-color: "&6"  # Interface color

# Anti-spam settings
anti-spam:
  enabled: false  # Enable/disable
  cooldown-seconds: 5  # Time interval
  max-messages-per-interval: 3  # Max messages per interval
```

## Anti-Spam

The anti-spam system prevents rapid chat spam and maintains chat quality. Administrators can:

- Enable/disable: `/chatstats antispam on|off`
- Set cooldown: `/chatstats antispam cooldown 5`
- Set limit: `/chatstats antispam maxmsg 3`

**How it works:**
- Tracks message sending speed for each player
- Blocks messages if player exceeds the limit within the time interval
- Blocked messages don't count toward statistics
- Each player has their own message counter
- Administrators are not affected by anti-spam

**Configuration Examples:**

Strict (prevent spam):
```yaml
anti-spam:
  enabled: true
  cooldown-seconds: 10
  max-messages-per-interval: 2
```

Standard (default):
```yaml
anti-spam:
  enabled: true
  cooldown-seconds: 5
  max-messages-per-interval: 3
```

Relaxed (allow active chat):
```yaml
anti-spam:
  enabled: true
  cooldown-seconds: 3
  max-messages-per-interval: 5
```

## Author

**SHTUKA**

## License

MIT License
