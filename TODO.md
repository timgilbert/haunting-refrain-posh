# Stuff to do

## Figure out datascript / posh / reframe

Things are a mite awkward with this right now. I'm storing the datascript
connection in the app-db, which among other things breaks the re-frisk
display. I'm using the re-frame effect stuff from 0.8, but a lot of the effect
handlers just transact a change into datascript and I'm relying on posh to
pull them out for UI changes.

This actually doesn't seem as bad as I expected it to be, but maybe I
should keep datascript around as a coeffect, inject it into a bunch of
my handlers, and then use dynamic subscriptions for the front-end.

## UI

- Play button for songs

## Playlist generation

- Re-roll seeds when no search results are found
- Keep track of what's been searched
- Generate and save an actual playlist
- Maybe try soundcloud instead of spotify, less walled-gardeny
  and you can stream music from the actual page
- Or youtube? Seems like a swamp, though

## Persistence

- Save checkin results to localstorage, only fetch new ones
- Same for spotify searches
- Look into persisting the whole datascript db - maybe via
  [datascript-transit](https://github.com/tonsky/datascript-transit)?

## Checkins

- Support Facebook checkins
- Maybe instagram or something?
- Add UI to select window of time to use for checkins

## Mobile

- Make it prettier
- Mobile interface via swipes / taps, shake.js

## Deployment

- Hook to CircleCI etc
- Deploy on successful master build (github pages? netlify?)
