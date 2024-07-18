# Pokemon-Challenge

https://gist.github.com/eric-klukovich/70610cc12bf28ffaa6fc3ade8e25bc57
https://docs.pokemontcg.io/sdks/kotlin/

## Stack

- Compose
- Navigation
- Koil
- ViewModel
- Retrofit
- OkHttp
- Kotlin serialization
- Coroutines
- Koin

## Architecture

- MVVM
- Clean
- Repository with Data sources

## Features

- List of Pokémons
- Pokemon detail

## Notes

Api Cache-Control header is overriden to 3600 seconds (1 hour) to avoid hitting the api too often due to rate limiting.