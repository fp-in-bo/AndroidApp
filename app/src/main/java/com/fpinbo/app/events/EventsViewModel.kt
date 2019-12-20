package com.fpinbo.app.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.unsafeRunAsyncInViewModel
import arrow.fx.IO
import arrow.fx.typeclasses.seconds
import javax.inject.Inject

class EventsViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableLiveData<EventsState>()

    val state: LiveData<EventsState>
        get() = _state

    init {
        loadData()
    }

    private fun loadData() {
        _state.value = Loading
        retrieveData()
            .unsafeRunAsyncInViewModel(this) { result ->
                val viewState = result.fold(
                    ifLeft = { Error(it.message.orEmpty()) },
                    ifRight = { Events(it.events) }
                )
                _state.postValue(viewState)
            }
    }

    private fun retrieveData(): IO<Events> {

        return IO.sleep(2.seconds).flatMap {

            IO.just(
                Events(
                    listOf(
                        Event(
                            title = "(IM)Practical Functional Programming, adopting FP in industry",
                            speaker = "Alessandro Zoffoli",
                            imageUrl = "https://scontent-fco1-1.xx.fbcdn.net/v/t1.0-9/78409006_10162737638040261_986400575954354176_o.jpg?_nc_cat=104&_nc_ohc=hiQmuclhmuAAQl56rV1Z8TU-eK4EeEPLgwJevwCCQ-bz_9gVAYnpuweRQ&_nc_ht=scontent-fco1-1.xx&oh=85e7121d1792569bd7a6b23f5a1c7b6f&oe=5E7C0C26",
                            description = "Fp is only a technology trend? In this talk Alessandro will give you a production example to answer this question!"
                        ),
                        Event(
                            title = "KindOf<Polymorphism>",
                            speaker = "Daniele Campogiani",
                            imageUrl = "https://scontent-fco1-1.xx.fbcdn.net/v/t1.0-9/72071204_10162500322720261_6644187464858599424_o.jpg?_nc_cat=111&_nc_ohc=HVNoKnl-MKQAQknY1qHaIRDw9Jw2D-Fz1qxzmgSsPqUkVUSM6B2j6PmFA&_nc_ht=scontent-fco1-1.xx&oh=2bf933781e0e3bc455a579382ced0fa4&oe=5E75D343",
                            description = "In this talk Daniele Campogiani (https://twitter.com/dcampogiani) will explore the tagless final approach abstracting over different effects in Kotlin.\n" +
                                    "\n" +
                                    " Suppose you are writing software and you have to choose how to model an async operation,\n" +
                                    "will you choose RxJava? Coroutines? Or maybe Arrow IO?\n" +
                                    "What if you can write you code based just on some behaviours and then let your future self choose the proper implementation for each scenario?\n" +
                                    "In this talk, we will use some concepts of functional programming that will enable us to do just that.\n" +
                                    "We will explore:\n" +
                                    "- Typeclasses\n" +
                                    "- Higher Kind\n" +
                                    "- Polymorphic Programs\n" +
                                    "\n" +
                                    "See you at 19:00 @LuogoComune.\n" +
                                    "The event will start with Pizza+Beer for 5 euros and will continue with the Daniele's talk.\n" +
                                    "See you there!"
                        ),
                        Event(
                            title = "Come risolvere un puzzle cripto-aritmetico in Scala",
                            speaker = "Filippo Vitale",
                            imageUrl = "https://scontent-fco1-1.xx.fbcdn.net/v/t1.0-9/56119863_10161671437970261_805852069364760576_o.jpg?_nc_cat=101&_nc_ohc=vrsOfluE_0MAQk0c_oR22W5iMFVwIZl3273y5yR325065mzK51dgPxnSA&_nc_ht=scontent-fco1-1.xx&oh=80adddae081a42c48f376a1c0064f852&oe=5EB214CA",
                            description = "Differenti approcci alla risoluzione di un CSP utilizzando Monad Transformer e un Algoritmo Genetico\n" +
                                    "\n" +
                                    "In questo talk verranno esplorati alcuni approcci funzionali per la risoluzione di CSP.Filippo (@filippovitale ) ci mostrerà a tal fine un utilizzo pratico di State Monad, List Monad, StateT e di un semplice algoritmo genetico. Sviluppatore  Scala (the functional way) per oltre 3 anni presso NETSCOUT (Arbor Networks), dopo piú di un decennio passato tra Java e C#. Attualmente lavora come Program Manager nel team ASERT nel campo Threat Intelligence per i sistemi di supporto alla mitigazione di attacchi DDOS. L'intervento di Filippo verrà anticipato come di consueto da un aperitivo a base di pizza e birra per un prezzo totale di 5 euro."
                        ),
                        Event(
                            title = "Functional Programming in applicazioni front-end development",
                            speaker = "Gabriele Petronella",
                            imageUrl = "https://scontent-fco1-1.xx.fbcdn.net/v/t1.0-9/52830911_10161546229200261_5668015615512674304_o.jpg?_nc_cat=110&_nc_ohc=o3Np1TfgZxoAQleafD9lEUe48s6ZMsdS4IbqSkFkk5_tXy_YCVHoqFXxA&_nc_ht=scontent-fco1-1.xx&oh=f8c061e93bcae3f33189a273ce725989&oe=5EB2B208",
                            description = "Come la programmazione funzionale può semplificare il front-end development\n" +
                                    "\n" +
                                    "La programmazione funzionale sta prendendo piede in moltissime aree dello sviluppo, ma questa cosa come può aiutare il front-end development?\n" +
                                    "\n" +
                                    "Gabriele (https://twitter.com/gabro27 ) ci illustrerà come la programmazione funzionale si pone nella scena dello sviluppo front-end, e come possiamo introdurla gradualmente all'interno di codebase esistenti.Gabriele è il co-founder di https://www.buildo.io/ .\n" +
                                    "\n" +
                                    " Affascinato dai linguaggi di programmazione in generale  e dalla programmazione funzionale, è attualmente uno sviluppatore principalmente Typescript e Scala. \n" +
                                    "\n" +
                                    "Dice di essere un  parlatore instancabile (a volte anche troppo instancabile) ed è proprio per questo che ha deciso di infastidire un'intera comunità di appassionati diventando uno speaker!Laureato presso la UIC di Chicago ha completato la sua formazione in Computer Science presso il Politecnico di Milano. \n" +
                                    "\n" +
                                    "Ad oggi  è anche uno degli organizzatori dello Scala Italy (http://2018.scala-italy.it )  e uno dei contributor più attivi all'interno della community scala (https://github.com/gabro).\n" +
                                    "\n" +
                                    "L'intervento di Gabriele verrà anticipato come di consueto da un aperitivo a base di pizza e birra per un prezzo totale di 5 euro."
                        ),
                        Event(
                            title = "L'arte della composizione",
                            speaker = "Marco Perone",
                            imageUrl = "https://scontent-fco1-1.xx.fbcdn.net/v/t1.0-9/46058304_317951442267953_2109324538612285440_o.jpg?_nc_cat=109&_nc_ohc=m80IhGVea4AAQkQ1w5q5qwSv1Wf7TDusiC7mD-pbbEz07SET-n_-4t0qA&_nc_ht=scontent-fco1-1.xx&oh=3f9782e74d6f6862e97ad2ed287ddb75&oe=5EB0CBFB",
                            description = "L'arte di comporre ci permette di costruire architetture complesse partendo da componenti semplici. Inoltre avere blocchi semplici e facilmente componibili ci permette di ottenere l'agognata capacità di riutilizzare parti del codice che abbiamo già scritto.\n" +
                                    "\n" +
                                    "La disciplina che studia e formalizza i meccanismi che regolano la composizione viene chiamata dai matematici \"teoria delle categorie\".\n" +
                                    "\n" +
                                    "In questo talk vedremo insieme quali sono le idee e i concetti principali che la teoria delle categorie ci offre, e ragioneremo sul perchè un tale approccio può essere interessante ed utile per un qualunque programmatore.\n" +
                                    "\n" +
                                    "---------------------------------------------------------------------------------------\n" +
                                    "Il nostro Speaker Marco Perone.\n" +
                                    "\n" +
                                    "Software bricklayer in MVLabs. \n" +
                                    "\n" +
                                    "\"Cresciuto come matematico, amo portare nel codice che scrivo la stessa precisione e lo stesso formalismo che si trova in matematica. Nel bel mezzo di una (im)mutazione verso approcci e linguaggi funzionali.\""
                        ),
                        Event(
                            title = "Who said it cannot be typed?\n",
                            speaker = "Claudio Sacerdoti Coen ",
                            imageUrl = "https://scontent-fco1-1.xx.fbcdn.net/v/t1.0-9/41991027_296323221097442_1961543810697658368_o.jpg?_nc_cat=111&_nc_ohc=_Q1TmZJEPPwAQkivb2uwuP9WDipS4u6xPJX9guk-eIkmfWjspxQosbPbQ&_nc_ht=scontent-fco1-1.xx&oh=b815e113510e313173310fe41a6c4c0a&oe=5E720566",
                            description = "Professor Claudio Sacerdoti Coen will drive us in the Generalized Algebraic Data Types world.\n" +
                                    "\n" +
                                    "Here's an abstract of the talk we are going to assist:\n" +
                                    "\n" +
                                    "\"Terms (also called expressions) do all the computational work. Types, if present at all, never get their hands dirty: they just stay still doing nothing, supervising if terms behave well. Many years ago some rebels wanted to put types to work too, by allowing computations in them as well. Dependent types were born, but types refused to cooperate and made the life of programmers so hard that only masochistic academics fell in love with them.\n" +
                                    "\n" +
                                    "Fast forward a few decades: finally somebody understood how to reach a compromise between complexity and expressivity: Generalized Algebraic Data Types (GADTs) were born. Currently, they are implemented (without run-time overhead over ADT!) both in OCaml and Haskell and there is on-going work to try to fit them in other languages as well (e.g. Scala). By using GADTs one can\n" +
                                    "1) type expressions and functions that escaped all previous type systems (e.g. printf-like functions)\n" +
                                    "2) impose at compile time invariants on the data structures to prevent errors\n" +
                                    "3) achieve more precise typing for already typable functions, improving clarity and reducing the size of the code and the need for dealing in the code with impossible cases\n" +
                                    "4) make data types abstract (i.e. hiding their implementation) in restricted parts of the code, flexibly\n" +
                                    "5) write complex functions and types that nobody sane will be able to decipher...\n" +
                                    "\n" +
                                    "I will try (and maybe fail..) to give a (very?) gentle introduction to the subject, assuming minimal knowledge on Abstract Data Types and OCaml/Haskell.\"\n" +
                                    "\n" +
                                    "Why you should attend this event?\n" +
                                    "\n" +
                                    "Make you curious about a still-considered-exoteric programming language construct that -- when used properly -- can really improve your code in several situations, adding zero run-time overhead. In the worst case you will be able at least to surprise your friends writing a type-safe printf in user space :-)\n" +
                                    "\n" +
                                    "===================================================\n" +
                                    "\n" +
                                    "Thanks to Luogocomune to host this event.\n" +
                                    "\n" +
                                    "Pizza and beer will be available before the event start for 5 euro."
                        ),
                        Event(
                            title = "Android Functional Validation with Arrow\n",
                            speaker = "Daniele Campogiani",
                            imageUrl = "https://scontent-fco1-1.xx.fbcdn.net/v/t1.0-9/34047633_219204312142667_9218565191061471232_o.jpg?_nc_cat=109&_nc_ohc=uxpk7aAmvh0AQnx0qWFKhzIFKF2pGw_V1APS2R64PBfgYBB0sMEZXzupg&_nc_ht=scontent-fco1-1.xx&oh=8a36b63ab5e922a3161a3c1439ccd7f9&oe=5E69FFE0",
                            description = "A meno di un mese di distanza dal nostro ultimo evento è con estremo piacere che annunciamo il nostro secondo incontro, questa volta con la collaborazione dei ragazzi del GDG Bologna - Google Developers Group.\n" +
                                    "\n" +
                                    "Come speaker avremo Daniele Campogiani con il suo talk:\n" +
                                    "\n" +
                                    "Android Functional Validation With Arrow\n" +
                                    "\n" +
                                    "La validazione di dati è un caso d'uso ideale per esplorare alcuni pattern di programmazione funzionale che possano rispondere a precise esigenze.\n" +
                                    "\n" +
                                    "La libreria Arrow è diventata la naturale estensione funzionale del linguaggio Kotlin che ci dà la possibilità, quindi, di applicare pattern ben noti nel mondo della programmazione funzionale per la risoluzione di problemi comuni come, ad esempio, la validazione di dati su piattaforma Android.\n" +
                                    "\n" +
                                    "Tramite un form di registrazione di un'applicazione Android avremo modo di analizzare insieme alcuni data type forniti da Arrow e valutarne l'efficacia nella risoluzione di classici problemi di validazione dati.\n" +
                                    "\n" +
                                    "Daniele Campogiani, Android software developer in YOOX Net-A-Porter e membro di Functional Programming in Bologna, è da sempre focalizzato sul mondo del mobile computing. \n" +
                                    "In questo ultimo anno ha maturato, tuttavia, esperienza in molteplici ambiti tra cui quello della programmazione funzionale della quale sta esplorando i benefici tramite il linguaggio Kotlin.\n" +
                                    "\n" +
                                    "Infine, ci fa piacere sottolineare che Daniele sarà presente come speaker alla prima conferenza italiana su Kotlin (https://milan.kotlincommunityconf.com/) con lo stesso talk che ci verrà presentato.\n" +
                                    "\n" +
                                    "Se voleste contattarlo ecco a voi qualche riferimento:\n" +
                                    "Twitter: https://twitter.com/dcampogiani\n" +
                                    "Personal Site: http://danielecampogiani.com  \n" +
                                    "\n" +
                                    "La serata inizierà con un light talk di Devid Farinelli che ci parlerà di Flutter e proseguirà con il talk di Daniele\n" +
                                    "\n" +
                                    "Infine, è prevista l'estrazione di una licenza Intellij fornita dai ragazzi del GDG Bologna - Google Developers Group.\n" +
                                    "\n" +
                                    "Per chi volesse mangiare e/o bere e vuole ordinare qualcosa, il locale Luogocomune metterà a disposizione un menu composto di pizza e birra ad un prezzo economico."
                        ),
                        Event(
                            title = "An evening with Matteo Baglini",
                            speaker = "Matteo Baglini",
                            imageUrl = "https://scontent-fco1-1.xx.fbcdn.net/v/t1.0-9/37611895_2060795997581700_4499163780852219904_n.jpg?_nc_cat=103&_nc_ohc=k_SUDgFkOUsAQnI3PbSKojr6IfjyhCKewS2yL66tWDRfaJUyPznhWJF6Q&_nc_ht=scontent-fco1-1.xx&oh=11658e8ed4fb72f1d545684057491642&oe=5EAA2F95",
                            description = "È con estremo piacere che annunciamo il nostro primo evento organizzato con gli amici di Avanscoperta e con la grande disponibilità di Off Bologna. \uD83D\uDC4F\n" +
                                    "\n" +
                                    "\uD83D\uDCA5 Come primo ospite avremo il mitico Matteo Baglini. \uD83D\uDC51\uD83D\uDD1D\n" +
                                    "\n" +
                                    "Il talk che Matteo ci presenterà: ⤵ \n" +
                                    "\n" +
                                    "▶ Onion Architecture in salsa Functional Programming ◀\n" +
                                    "\n" +
                                    "L'assenza dei side-effect nella programmazione funzionale pura permette di creare funzioni semplici da comprendere, comporre e testare in isolamento. \n" +
                                    "Nonostante questo, senza una chiara struttura, il risultato sarà un guazzabuglio di funzioni che mescolano diversi \"concerns\", livelli di astrazione e responsabilità.\n" +
                                    "Negli anni la Onion Architecture, grazie alla sua struttra a layer concentrici, ha dimostrato di essere un modello efficace per ottenere modularità, componibilità e testabilità su larga scala. Durante questo talk, codice alla mano, vedremo come questi due concetti si completano a vicenda.\n" +
                                    "\n" +
                                    "\uD83D\uDD75 Matteo, co-founder di doubleloop.io, è da sempre focalizzato su semplicità e costo del cambiamento in modo da creare codebase che si adattino facilmente alla continua evoluzione del business. \n" +
                                    "\uD83D\uDCBB Matteo ha maturato esperienza in molteplici domini ed ambienti tecnologici (web, desktop, embedded, mobile), con una forte attenzione a clean code, design evolutivo e architettura.\n" +
                                    "\n" +
                                    "\uD83C\uDF7B Per chi volesse mangiare e/o bere e vuole ordinare qualcosa, il bar di Off Bologna resterà aperto durante la serata. "
                        )
                    )
                )
            )
        }
    }
}