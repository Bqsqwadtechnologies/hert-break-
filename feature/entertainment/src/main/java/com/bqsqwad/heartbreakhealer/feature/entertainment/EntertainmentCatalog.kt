package com.bqsqwad.heartbreakhealer.feature.entertainment

data class StoryPart(val number: Int, val title: String, val paragraphs: List<String>)
data class StorySeries(val id: String, val title: String, val tagline: String, val parts: List<StoryPart>)

object EntertainmentCatalog {
    val stories = listOf(
        StorySeries(
            id = "letters_after_rain",
            title = "Letters After the Rain",
            tagline = "A chance meeting, an old promise, and a letter that was never meant to be found.",
            parts = listOf(
                StoryPart(1, "The Bookshop at Closing Time", listOf(
                    "Mara had promised herself she would never again mistake a grand gesture for a good heart. So when the rain trapped her inside a little bookshop on Broad Street, she was perfectly happy to keep to herself, browse the shelves, and wait for the storm to pass.",
                    "Then a stranger reached for the same battered novel. Their fingers brushed. He pulled back with an apologetic smile, but his expression changed when he noticed the name written inside the cover: Mara Adebayo.",
                    "“You knew my mother?” she asked, surprised by the way his smile disappeared.",
                    "He looked toward the rain-streaked window. “I think your mother knew someone in my family. And I think this book has been waiting for you.”",
                    "Before Mara could ask another question, the shopkeeper called the stranger by a name she had not heard in years: Daniel."
                )),
                StoryPart(2, "A Name in the Margins", listOf(
                    "Mara did not follow Daniel outside. She did not ask for his number, and she certainly did not accept the mysterious book without an explanation. Instead, she opened it beneath the warm yellow shop lamp.",
                    "The margins were filled with neat handwriting. Some notes were ordinary—little thoughts about a character or a line of poetry. Others were dates. Beside one date, her mother's handwriting said: “If he comes back, tell him the truth.”",
                    "Daniel waited by the door, giving her room to decide. When she finally looked up, he placed a small envelope on the counter.",
                    "“I was asked to give you this only if you found the book yourself,” he said.",
                    "The envelope carried her mother's handwriting. It was sealed with a tiny blue sticker, and on the front were five words: “Read this before you trust me.”"
                )),
                StoryPart(3, "The First Letter", listOf(
                    "At home, Mara placed the envelope beside a cup of tea and stared at it until the tea went cold. She had spent months learning how to move forward. Now the past had arrived in a raincoat and left a letter on her table.",
                    "The letter was gentle, not dramatic. Her mother wrote about a friendship she had lost, a promise made too quickly, and a young man who had carried a misunderstanding for far too long.",
                    "There was no demand that Mara forgive anyone. There was only a request: meet Daniel once, hear his side, and decide for herself.",
                    "At the bottom, a final sentence had been underlined twice: “The person who brings you this letter is not the person you should be afraid of.”",
                    "Mara turned the page over. A second message had been written there in different ink: “But ask him why he waited twelve years.”"
                )),
                StoryPart(4, "Coffee and Questions", listOf(
                    "Daniel chose a quiet café with wide windows and no music loud enough to hide an awkward silence. He arrived early, ordered nothing for her, and let her choose her own seat.",
                    "Mara appreciated that. She asked about the book, the letters, and the twelve years. Daniel answered carefully. His father and her mother had once planned a community library together. A disagreement had ended the friendship, and both families had quietly carried different versions of the story.",
                    "“Why now?” Mara asked.",
                    "Daniel looked at his hands. “Because I found the last missing page. And because I was tired of letting a story about our parents decide what we were allowed to become.”",
                    "He slid a photograph across the table. Mara recognised the library. What she did not recognise was the little girl standing beside her mother—wearing Mara's own childhood bracelet."
                )),
                StoryPart(5, "The Photograph", listOf(
                    "The photograph was dated the summer Mara turned six. She remembered the bracelet, the yellow dress, and the library's old blue door. She did not remember the boy beside her mother.",
                    "Daniel explained that the boy was his older brother, Elias. Elias had been the one who found the missing page, but he had disappeared from the family years ago after an argument no one wanted to discuss.",
                    "Mara studied the photograph again. On the back, someone had written: “For the children, when they are old enough to finish what we started.”",
                    "“What were they supposed to finish?” she asked.",
                    "Daniel opened his notebook. Inside was a sketch of the library's reading room, with a small box drawn beneath the floorboards. Beside it, in her mother's handwriting, were the words: “The promise is hidden where the stories begin.”"
                )),
                StoryPart(6, "The Blue Door", listOf(
                    "The old library had become a storage building, but the blue door was still there. Mara and Daniel visited on a Saturday afternoon with permission from the caretaker, who remembered both families and seemed unsurprised to see them together.",
                    "They searched the reading room, comparing the sketch with the walls. Behind a shelf, Mara found a loose wooden panel. It opened onto a narrow compartment containing a small brass key and a folded list of names.",
                    "Her mother's name was first. Daniel's father was second. The other names belonged to people who had helped build the library.",
                    "At the bottom was a name neither of them expected: Elias.",
                    "The caretaker went very still. “If Elias is on that list,” she whispered, “then he came back here last week.”"
                )),
                StoryPart(7, "Someone Was Here", listOf(
                    "The caretaker said Elias had asked about the box, then left before she could tell him what was inside. She described him as polite, tired, and carrying a photograph of the same blue door.",
                    "Daniel tried calling his brother. The number no longer worked. Mara noticed a fresh footprint near the loose panel and a scrap of paper caught beneath the shelf.",
                    "It held only an address and a time: Sunday, 6:00 p.m.",
                    "“This could be a trap,” Daniel said.",
                    "“Or an invitation,” Mara replied. “We can choose to go together, in a public place, and leave if anything feels wrong.”",
                    "Daniel nodded. For the first time, Mara saw something beyond worry in his face: hope."
                )),
                StoryPart(8, "The Man at the Window", listOf(
                    "The address led to a small community hall hosting a book donation event. Families moved between tables, children carried picture books, and volunteers sorted boxes. Nothing about the room felt secret.",
                    "Then Mara saw a man standing by the window. He looked older than the boy in the photograph, but the eyes were unmistakable.",
                    "Daniel stopped walking. “Elias.”",
                    "The man turned. For a moment, nobody moved. Then Elias looked at Mara and said, “You have your mother's expression when you're trying not to cry.”",
                    "He reached into his jacket and took out a second envelope. “Before I explain anything, you need to know that the letter you read was not the last one your mother wrote.”"
                )),
                StoryPart(9, "The Missing Page", listOf(
                    "Elias explained that the final letter had been kept by their father, who believed revealing the truth would reopen an old wound. He had only recently discovered that the letter was addressed to both families, not to one person.",
                    "Inside was a simple apology and a plan to reopen the library as a place where young people could learn, read, and feel welcome. The disagreement had never been about money, as the families had been told. It had been about who would receive credit.",
                    "Mara felt the years of silence settle into something smaller. Not harmless, not easy—but no longer mysterious.",
                    "Daniel looked at his brother. “Why didn't you tell me?”",
                    "Elias lowered his eyes. “Because I made a promise to someone else first. And that person is here tonight.”"
                )),
                StoryPart(10, "The Promise", listOf(
                    "A woman approached from the far side of the hall. She carried a folder of old plans and introduced herself as the architect who had helped their parents design the library.",
                    "She had kept the original agreement, signed by both families. It showed that the library had been intended as a shared project from the beginning.",
                    "Mara read the signatures twice. Her mother had not been betrayed. Daniel's father had not stolen anything. Two proud people had misunderstood one another, and everyone after them had inherited the silence.",
                    "Outside, the rain began again. Daniel asked whether Mara wanted to walk with him to the bus stop. No promises, no grand speeches—just a question.",
                    "She smiled. “Only if you tell me what happened to the other half of the photograph.”"
                )),
                StoryPart(11, "The Other Half", listOf(
                    "Daniel laughed softly and took a small photograph from his wallet. It was the missing half: two children standing in front of the blue library door, holding the same book between them.",
                    "Mara recognised herself. The boy beside her was Daniel. They had met once, long before either could remember, and their mothers had written a note on the back: “May they grow up knowing that kindness can begin again.”",
                    "The discovery did not make them destined for one another. It did something better: it gave them a beginning they could choose for themselves.",
                    "Weeks later, the library project reopened. Mara and Daniel worked side by side, slowly learning each other's habits, humour, and hopes. Their friendship grew without hurry, and the possibility of something more arrived quietly.",
                    "On opening night, Mara found one final envelope inside the old book. It contained a note in her mother's handwriting: “If you have reached this page, the story is yours now. Write it honestly.”"
                ))
            )
        )
    )

    val jokes = listOf(
        "I told my pillow all my problems. It said, “I’m here for you.” Then it got pressed under pressure.",
        "My alarm clock and I are in a serious relationship. Every morning, it starts drama and I hit snooze.",
        "Why did the phone go to school? It wanted to improve its reception.",
        "I tried to write a joke about a broken pencil, but it had no point.",
        "Why did the tomato blush? It saw the salad dressing.",
        "My Wi-Fi and I have trust issues. It disappears whenever I need it most.",
        "I asked my fridge for advice. It told me to chill.",
        "Why did the bicycle fall over? It was two-tired.",
        "I started a band called 999 Megabytes. We still haven't got a gig.",
        "Why don't eggs tell jokes? They might crack each other up.",
        "I told my shoes a secret. Now they are always tongue-tied.",
        "Why did the calendar look worried? Its days were numbered.",
        "My favourite exercise is a cross between a lunge and a crunch. I call it lunch.",
        "Why was the maths book sad? It had too many problems.",
        "I wanted to become a baker, but I couldn't raise the dough.",
        "Why did the computer get cold? It left its Windows open.",
        "I bought a belt made of watches. It was a waist of time.",
        "Why did the music teacher need a ladder? To reach the high notes.",
        "My dog swallowed a watch. Now he has a tick in his stomach.",
        "Why did the orange stop halfway up the hill? It ran out of juice.",
        "I used to hate facial hair, but then it grew on me.",
        "Why did the coffee file a police report? It got mugged.",
        "I tried to catch fog yesterday. Mist.",
        "Why did the student eat his homework? The teacher said it was a piece of cake.",
        "My wallet is like an onion. Opening it makes me cry.",
        "Why are fish so smart? They live in schools.",
        "I asked the ocean to keep a secret. It said, “I can't; I always wave.”",
        "Why did the banana visit the doctor? It wasn't peeling well.",
        "I used to be addicted to soap, but I'm clean now.",
        "Why did the scarecrow win an award? He was outstanding in his field.",
        "I told a joke about construction. I'm still working on it.",
        "Why did the cookie go to the hospital? It felt crummy.",
        "My bed and I are perfect for each other, but my alarm clock keeps trying to separate us.",
        "Why did the golfer bring two pairs of trousers? In case he got a hole in one.",
        "I named my printer Bob Marley. It keeps jamming.",
        "Why did the picture go to jail? It was framed.",
        "I wanted to learn time travel, but I didn't have the time.",
        "Why did the grape stop in the middle of the road? It ran out of juice.",
        "My phone battery is like my social energy: impressive at 100%, mysterious at 2%.",
        "Why was the broom late? It swept in.",
        "I asked my shoes why they were tired. They said they had been through a lot of sole-searching.",
        "Why did the stadium get hot after the game? All the fans left.",
        "I made a pencil with two erasers. It was pointless.",
        "Why did the cow become an astronaut? It wanted to visit the moooon.",
        "I told my suitcase there would be no holiday this year. Now it has emotional baggage.",
        "Why did the keyboard break up with the mouse? It felt they were going in different directions.",
        "I tried to organise a hide-and-seek tournament, but good players are hard to find.",
        "Why did the baker go to therapy? He kneaded it.",
        "My plants are great listeners. They never interrupt; they just leaf it to me.",
        "Why did the light bulb get promoted? It was very bright.",
        "I asked the elevator for a joke. It said the humour was on another level."
    )
}
