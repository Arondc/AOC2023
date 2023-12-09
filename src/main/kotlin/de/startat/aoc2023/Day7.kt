package de.startat.aoc2023

import de.startat.aoc2023.HandType.*
import java.util.*

data class D7Play(val cards: List<Card>, val bid: Int) {
    val type: HandType
    val jokeredType : HandType
    init {
        fun determineType(groupedCards : SortedMap<Int, List<Card>>) : HandType = when {
            groupedCards.any { (_, v) -> v.size == 5 } -> FiveOfAKind
            groupedCards.any { (_, v) -> v.size == 4 } -> FourOfAKind
            groupedCards.any { (_, v) -> v.size == 3 } && groupedCards.any { (_, v) -> v.size == 2 } -> FullHouse
            groupedCards.any { (_, v) -> v.size == 3 } -> ThreeOfAKind
            groupedCards.filter { (_,v) -> v.size == 2 }.count() == 2 -> TwoPair
            groupedCards.any { (_,v) -> v.size == 2 } -> OnePair
            else -> HighCard
        }
        val groupedCards = cards.groupBy { it.value }.toSortedMap { a, b -> a.compareTo(b) }
        type = determineType(groupedCards)

        val jokerValues = listOf('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A')
        val jokeredTypes = jokerValues.map { jv ->
            val jGroupedCards = cards.map { c ->
                if (c.face == 'J') Card(
                    jv,
                    cardValuesS2[jv]!!
                ) else c
            }. groupBy { it.value }.toSortedMap { a, b -> a.compareTo(b) }
            determineType(jGroupedCards)
        }.toList()
        jokeredType = jokeredTypes.min()
    }
}

enum class HandType{
    FiveOfAKind,FourOfAKind,FullHouse,ThreeOfAKind,TwoPair,OnePair,HighCard
}

class Card(val face: Char, val value: Int) : Comparable<Card> {
    companion object {
        fun ofStar1(char: Char) = Card(char, cardValues[char]!!)
        fun ofStar2(char: Char) = Card(char, cardValuesS2[char]!!)
    }

    override fun compareTo(other: Card): Int = other.value.compareTo(this.value)
    override fun toString(): String = "$face|$value"

}

val cardValues = mapOf(
    '2' to 2,
    '3' to 3,
    '4' to 4,
    '5' to 5,
    '6' to 6,
    '7' to 7,
    '8' to 8,
    '9' to 9,
    'T' to 10,
    'J' to 11,
    'Q' to 12,
    'K' to 13,
    'A' to 14,
)

val cardValuesS2 =mapOf (
    'J' to 1,
    '2' to 2,
    '3' to 3,
    '4' to 4,
    '5' to 5,
    '6' to 6,
    '7' to 7,
    '8' to 8,
    '9' to 9,
    'T' to 10,
    'Q' to 12,
    'K' to 13,
    'A' to 14,
)

class Day7 {
    fun star1() {
        var plays = parsePlays1(day7input)
        plays = sortPlays1(plays)
        plays.forEachIndexed{ idx,p -> println("${idx+1} $p ${p.type}") }
        val winnings = calculateWinnings(plays)
        println(winnings)

    }

    fun star2() {
        var plays = parsePlays2(day7input)
        plays = sortPlays2(plays)
        plays.forEachIndexed{ idx,p -> println("${idx+1} $p ${p.jokeredType}") }
        val winnings = calculateWinnings(plays)
        println(winnings)
    }

    private fun calculateWinnings(plays: List<D7Play>) =
        plays.mapIndexed { idx, p -> (idx + 1) * p.bid }.reduce { acc, p -> acc + p }

    private fun sortPlays1(plays: List<D7Play>) = plays.sortedWith(
        compareBy<D7Play> { it.type }.thenBy { it.cards[0] }.thenBy { it.cards[1] }.thenBy { it.cards[2] }
            .thenBy { it.cards[3] }.thenBy { it.cards[4] },
    ).reversed()

    private fun sortPlays2(plays: List<D7Play>) = plays.sortedWith(
        compareBy<D7Play> { it.jokeredType }.thenBy { it.cards[0] }.thenBy { it.cards[1] }.thenBy { it.cards[2] }
            .thenBy { it.cards[3] }.thenBy { it.cards[4] },
    ).reversed()

    private fun parsePlays1(input : String) = input.lines().map { line ->
        val handAndBid = line.split(" ")
        val cards: List<Card> = handAndBid[0].map { Card.ofStar1(it) }
        val bid = handAndBid[1].toInt()
        D7Play(cards, bid)
    }

    private fun parsePlays2(input : String) = input.lines().map { line ->
        val handAndBid = line.split(" ")
        val cards: List<Card> = handAndBid[0].map { Card.ofStar2(it) }
        val bid = handAndBid[1].toInt()
        D7Play(cards, bid)
    }


}

val day7testInput = """
	32T3K 765
	T55J5 684
	KK677 28
	KTJJT 220
	QQQJA 483
""".trimIndent()

val day7input = """
    676AA 840
    5J666 204
    J4563 922
    7373J 199
    64882 944
    TK9KK 457
    54JA5 787
    T8889 294
    96TTT 503
    88T88 662
    J333A 524
    K8T82 994
    23552 253
    2T8JJ 588
    26772 972
    J6Q6T 777
    36A2A 5
    KKKK3 509
    T2T2T 793
    9T49T 326
    66QAQ 442
    2TJ87 572
    QKJK4 824
    5A755 107
    3847K 556
    K7682 855
    AJAJ3 882
    44499 830
    4J4KK 110
    5AA5A 489
    J5578 497
    A3583 992
    9A58A 452
    Q2366 703
    AA22J 749
    5J522 534
    Q7JQ7 802
    7757Q 43
    27338 239
    TTT24 485
    6488K 495
    22QJQ 936
    9AAJQ 194
    222J9 959
    QJA4K 809
    2QQTQ 59
    9J22J 109
    44A33 714
    A5AAJ 600
    34QKK 924
    698JA 148
    82229 661
    22423 951
    AA495 516
    A6J99 483
    366A3 818
    5549T 863
    2A22A 397
    ATKQ5 746
    999K5 717
    KQ622 375
    4J448 722
    KK55A 707
    JJ224 622
    555J3 190
    7J74J 913
    33AA8 570
    22K23 522
    3J7A7 200
    53533 843
    2Q5JJ 362
    224QQ 638
    8AA98 17
    T99TT 916
    Q94QQ 856
    88Q68 606
    824T4 289
    8937Q 926
    959A6 1000
    274KA 407
    74Q6K 72
    93Q9K 828
    486Q4 21
    4KT94 702
    AAA22 884
    TT882 304
    AAAT7 112
    99399 138
    999KJ 317
    9KT7Q 346
    TK99Q 644
    39JQ7 247
    88585 615
    K83J8 981
    QQQQK 625
    66AKK 171
    244JT 538
    Q5394 12
    KJJJK 458
    88KK8 724
    65566 993
    JT89Q 45
    T88TT 740
    22299 618
    8J73J 426
    3JQJA 558
    TT66T 804
    Q6A74 654
    66844 369
    A5A6A 413
    96TK3 161
    33262 365
    KKA39 225
    J22T7 288
    AKA96 37
    A2KQ7 473
    92A83 871
    44555 642
    33533 797
    J2322 885
    3J769 743
    8Q866 812
    QKKQQ 898
    83Q94 946
    8T3QK 233
    J3Q3Q 823
    73J94 157
    AQJ5A 985
    JTTTT 469
    8AA2K 906
    J92T9 983
    2T4T4 803
    3677J 596
    4QAA3 505
    666Q6 817
    QQQAA 351
    878QQ 541
    6T622 178
    33838 368
    A2822 366
    TT6AA 811
    873AK 518
    KKKK8 964
    93833 682
    KQJQK 16
    AAAA3 242
    54J89 866
    92A9K 982
    4QQQ6 665
    QT979 185
    36666 331
    A4AAA 301
    3QJQ2 590
    K4298 460
    Q66QJ 356
    4888J 70
    K3K55 149
    47478 532
    QQQTT 11
    QQ2Q5 295
    8588J 719
    5A3JK 65
    54225 106
    J8QQK 324
    KKQQ8 124
    A2887 617
    AQ2A6 907
    2J72Q 711
    AAAJA 248
    JJA27 91
    92J8Q 837
    A8883 121
    66446 341
    888JQ 63
    55296 651
    332J8 555
    T82J6 496
    38538 241
    32322 917
    2T333 562
    Q589J 891
    35K55 419
    23Q2A 685
    Q3Q3Q 293
    A8739 723
    55599 114
    77733 424
    KKJJA 49
    64J36 581
    T3A5K 601
    35A2K 563
    3KK5K 453
    55A66 383
    62222 699
    AAJ23 209
    33343 394
    9JJ99 921
    A92TA 672
    TT4T4 955
    5T593 101
    KKJ55 763
    33383 833
    66566 22
    82272 645
    4AJ27 914
    63966 523
    6K246 312
    Q48J7 54
    K77TK 174
    J7578 510
    KA6AA 342
    9K898 798
    9AT97 175
    75555 493
    22522 713
    6A2KA 647
    Q73Q3 561
    A72A7 627
    87J43 491
    QQ3TQ 410
    766T7 938
    A666A 280
    8AAJA 998
    988J9 989
    T4TT6 147
    42A2Q 860
    K8J75 637
    9A9Q7 376
    AKKJA 911
    KA464 582
    JJ59T 353
    9AAAA 751
    22K56 781
    777K7 552
    9TJ88 111
    T46KK 551
    2963K 593
    53358 205
    A4T87 267
    77667 886
    23QA3 197
    Q8745 632
    9K888 29
    AA2J4 623
    JTATT 78
    24242 800
    9Q94Q 206
    8T989 939
    46556 321
    3A33A 140
    KT345 382
    99A97 98
    KJK4A 28
    TJ54J 732
    2J789 877
    22AJ2 847
    KK222 849
    55AA6 58
    3J333 115
    9KK37 126
    9666A 995
    6A833 821
    79J5T 487
    TT7J7 415
    56349 372
    T9723 229
    42JA4 734
    KKQ33 954
    AA444 721
    2K222 151
    44445 928
    A3894 244
    Q88T8 436
    88555 888
    46666 850
    8J222 492
    4QJQ4 193
    2J2J2 842
    44644 814
    T999T 650
    9A643 223
    TK9A9 484
    4J546 810
    3KKK7 62
    K4499 327
    56568 38
    2AJKQ 952
    4774A 657
    547JJ 806
    89949 571
    246Q7 8
    33AA6 116
    76T64 254
    88899 323
    66T86 736
    6KKK7 827
    77666 224
    K4T4T 792
    Q68QQ 839
    KTKT6 292
    TQK26 88
    63T9A 905
    2292A 434
    8QQQQ 39
    53872 363
    99JAK 704
    7J273 795
    QQQ77 23
    92492 774
    886TT 975
    683K2 35
    46857 759
    K2549 389
    468AA 526
    27A22 521
    J4J29 565
    85T34 836
    56542 359
    K48AK 835
    8987T 908
    46644 347
    495QK 678
    22Q84 373
    78775 60
    33929 881
    QQQQA 466
    55537 15
    223KK 261
    QQ363 949
    2TTTT 135
    AAA8A 468
    2A68J 227
    88883 319
    5QJ76 664
    5K357 635
    8K68K 3
    99499 567
    A6292 213
    6T9KT 421
    9K999 64
    T8675 344
    4Q44Q 46
    88488 990
    44A44 235
    A5929 861
    55J56 266
    A77AQ 336
    7757K 471
    QQQQJ 291
    K7J96 480
    33239 652
    J2AAA 263
    77477 929
    J6TJT 540
    5J5A5 290
    A5A7J 776
    28K96 260
    2255Q 958
    K3596 18
    2JAJT 900
    K5555 329
    57385 102
    3T528 130
    57775 461
    TTTT9 942
    3TQ35 160
    K8333 335
    JJKKK 53
    74477 865
    84K3K 187
    245J3 86
    5396T 310
    T9A37 963
    K2J2K 528
    89233 991
    7547J 499
    J9TKT 919
    KK8QK 196
    5575T 779
    58528 84
    85228 584
    JJTTT 367
    QQQTQ 853
    9J999 619
    58JT5 862
    2227T 609
    66K6K 683
    7Q58Q 411
    666J4 71
    6877Q 816
    A5555 400
    K779A 848
    7A66K 195
    K2TK2 602
    7QQ9T 962
    J38QK 659
    97789 603
    K887K 700
    AJAA4 557
    TQ428 449
    79JQ4 219
    QKKKK 527
    999TQ 81
    92992 667
    2T22T 4
    T58AA 566
    J9595 154
    58685 2
    82J85 478
    4J444 692
    K7877 24
    98899 74
    2333J 298
    9J327 950
    8K694 826
    2K9A5 639
    JT7J6 459
    K2A77 728
    6T7TT 385
    KKKKA 343
    K28J8 144
    6668A 883
    7Q7J4 927
    A3A6A 741
    9JT68 760
    66363 273
    4848J 320
    45454 339
    33663 752
    TJT5T 438
    666A6 504
    J5J55 851
    23222 808
    48443 834
    95K95 258
    8J2KK 656
    44844 40
    4TAJ5 744
    8947T 546
    222KQ 455
    TKK99 515
    6K55J 604
    A32J9 841
    K4444 99
    K59A4 349
    TA8AA 819
    24J72 429
    4939K 535
    KQ3K8 318
    2AKJ3 500
    KKATT 517
    JT752 454
    3A2A5 550
    46JTK 901
    KKKTK 202
    66662 511
    63665 681
    2AKQ3 640
    7J692 542
    4KK8K 393
    33AAT 904
    KK5KK 669
    TT333 218
    95Q6J 807
    44334 401
    8Q888 931
    AAAQA 620
    K6KK8 42
    5TJ88 433
    TTJT2 737
    986JQ 113
    75JA2 467
    833A9 357
    82Q4J 432
    AKTAK 965
    68886 694
    35A93 137
    33939 974
    K454K 980
    K9JKT 14
    QQ34Q 910
    4A6T5 896
    93953 646
    382J9 127
    32726 377
    75999 537
    9392Q 697
    A33AA 128
    6584Q 237
    A66AQ 894
    67777 720
    63333 674
    T74Q2 846
    T2222 756
    AJTQA 630
    48848 984
    55636 392
    3T8J4 948
    5Q55J 575
    6633Q 108
    52K63 441
    QTTTQ 337
    84688 48
    T7777 123
    666T6 738
    22227 381
    2522A 757
    55448 285
    37389 633
    JJJJJ 189
    79799 259
    82TTJ 747
    9T95T 477
    55587 610
    499J9 416
    TT5TT 612
    29A53 414
    5T5T5 36
    92279 435
    79T22 61
    QA666 398
    428K2 785
    8883J 13
    5577K 282
    TJ78T 332
    JA97J 238
    TJ52K 214
    QQ23Q 388
    Q8Q5Q 786
    Q69Q9 333
    TTJKT 531
    7JA62 680
    52KJJ 987
    77876 153
    J3KK8 966
    7Q7Q6 420
    T4TA6 228
    4T265 83
    33933 873
    83384 771
    68K6K 340
    33A9A 668
    T7J33 50
    3J76K 559
    AAKK3 673
    KJQQ6 322
    24A4A 539
    T45J4 969
    3K5A3 428
    AA6A6 687
    2K788 380
    7A975 971
    8JQQ8 895
    Q245J 574
    QJQQJ 281
    T8QTQ 405
    4244J 624
    QT5J9 350
    4JKK7 51
    JJ977 262
    A8282 360
    A958T 446
    64Q43 822
    K5K2K 348
    J4TT4 684
    27K98 150
    48KJ7 501
    9AA89 44
    J4T3T 265
    49878 472
    73798 864
    34335 69
    254K6 255
    J77JK 589
    4KTJQ 256
    287JJ 708
    54TQ7 230
    Q4KK4 727
    565Q6 758
    A7AAA 80
    Q3K94 276
    2J89K 897
    JKJK8 286
    28AK6 598
    432KA 903
    4888K 852
    J77QT 870
    56J56 636
    KKKJ2 180
    78877 134
    7JJJ7 257
    T8T8J 25
    6TTTT 979
    KKKJK 79
    4Q6JJ 370
    J3A37 361
    44434 27
    J8868 139
    AAK8K 172
    KQ8A8 440
    Q6696 641
    55525 796
    6JJJ6 595
    45JTQ 594
    3K25K 250
    AA343 671
    5533T 173
    674Q5 465
    26999 34
    9A9A9 677
    5QT55 119
    22229 20
    3JQQJ 947
    2626A 564
    K5K5K 163
    K7K7J 611
    79769 854
    K8KQ8 374
    5726K 437
    A66Q8 390
    956J9 569
    TT788 794
    52867 791
    22A6J 406
    97999 986
    K7748 100
    TQ953 82
    99A99 474
    2T223 136
    8666J 519
    33723 303
    TT3TT 243
    J3QQ4 125
    787JJ 26
    A2QAQ 626
    JJJJ6 371
    92JQQ 482
    36Q6T 167
    AAAA5 451
    3555T 597
    K3K33 513
    5228Q 252
    5T393 967
    T8T88 953
    QQ464 427
    56558 735
    25822 226
    546Q3 879
    555T5 66
    5JQ57 945
    8338K 768
    2J222 358
    QQ44Q 418
    3KJ98 536
    85644 355
    939AA 67
    A25A2 6
    9TTJ2 655
    8K666 973
    JT83K 391
    76J77 876
    QQT55 447
    2K25K 957
    52J22 545
    2AT94 354
    K22J2 1
    33935 152
    K6J3T 709
    K3J38 813
    9J9KK 498
    QTJ24 832
    J5454 634
    86KKJ 770
    4386K 338
    KKQKQ 762
    94T4T 578
    25552 328
    Q8835 742
    767Q7 631
    Q5K55 613
    64287 307
    22666 30
    44494 915
    KAAKA 867
    78Q77 520
    TK4TT 553
    7Q77J 417
    38Q4J 297
    666JK 456
    A8KAA 143
    A997A 912
    QQQ66 977
    3A37A 210
    T3T37 716
    5954J 412
    2Q22Q 384
    79J54 270
    QJA8Q 221
    43A8T 512
    62228 815
    6569A 203
    A6AAJ 878
    5J545 616
    75557 249
    9QTQQ 245
    52626 695
    A77J7 182
    78QJ8 543
    AJAJA 605
    74K82 729
    77J22 874
    34T5J 766
    88337 548
    9T794 423
    32662 887
    298TT 621
    694J8 95
    46995 166
    J333J 778
    2AA92 890
    23288 679
    67T94 755
    T7242 893
    KK7K7 789
    55455 838
    A899T 302
    9T555 220
    2A555 731
    QQTJ7 643
    99599 184
    K746A 399
    8798A 159
    TJ63T 395
    999T9 573
    K8KT8 857
    67666 753
    KKK44 675
    744KK 628
    232J4 155
    97777 706
    4T446 507
    7463T 933
    35862 629
    J9Q8K 334
    88348 345
    57T67 845
    999J6 868
    K4QQ7 968
    QQA9Q 997
    449QQ 892
    64K32 169
    855TT 689
    899J6 488
    KQ78J 296
    66858 754
    99Q38 653
    T253J 576
    75J27 162
    J8JJJ 494
    6KKKK 402
    6963T 31
    Q444A 686
    666J6 745
    7J777 544
    T8TA8 772
    9AQAQ 978
    Q8292 19
    33K33 750
    AJ399 156
    48AJK 476
    TQT33 179
    83A5K 131
    32J2T 92
    J2J28 663
    7QJ75 314
    J8T38 475
    6K3AT 7
    TTJ7J 932
    7T584 820
    Q5497 32
    T33TT 490
    J69J6 782
    A5576 999
    38A38 77
    63686 533
    38838 103
    88887 309
    77769 464
    57335 439
    9T8TK 909
    53J33 599
    8K49T 222
    22T28 701
    75756 207
    777JJ 216
    2TQJT 271
    557Q7 236
    K8TTK 47
    JK67J 275
    36524 181
    TAJ8T 718
    74397 448
    2JQ92 158
    3J838 765
    4582A 97
    QK933 956
    66222 93
    86JQ5 443
    7JJ42 502
    5T5A5 607
    4ATTT 299
    JJ8J8 305
    TTATA 996
    9KK9K 872
    3AJAA 725
    A4242 387
    97K57 408
    7K677 445
    99429 94
    393Q6 805
    J7K77 306
    JTAJ6 767
    Q6447 696
    972AT 710
    2QQQ2 90
    Q4Q4K 403
    3KJ3K 56
    55485 930
    7J577 462
    9ATA8 614
    T9J79 712
    59J64 170
    89TT3 10
    86866 586
    AJ924 118
    555J5 396
    36J36 211
    6TJJK 240
    4447K 730
    33AAJ 941
    AQ24A 829
    569Q2 943
    44K4K 234
    9Q263 773
    55T58 923
    J6J66 902
    3734J 141
    Q7Q4J 278
    QQ5QA 587
    TT9AT 579
    TKK3T 75
    89KK2 269
    QQQQ6 961
    332K6 698
    33J3Q 186
    K9875 268
    696A9 330
    TJA43 858
    9Q66Q 76
    2TJTJ 799
    28866 775
    J444J 880
    Q8K54 470
    3KQ84 33
    JJ443 177
    J6TTK 274
    AT66A 217
    98888 889
    99998 283
    TQ2KA 554
    622J9 379
    59T5T 188
    27722 287
    54778 769
    Q84AJ 783
    9TTT7 264
    4J42Q 690
    32JTT 165
    4QQQQ 918
    3QQJQ 325
    883A4 486
    3Q284 201
    AKAAA 251
    73JA5 164
    72626 705
    A7342 514
    42222 272
    7J32K 404
    JK4T3 117
    33373 215
    QA256 284
    T4T4A 899
    TTATT 825
    55KK2 934
    J929A 246
    QJ4K8 425
    5A837 279
    JJ888 431
    KQKJ6 386
    AJ8A8 89
    JJJA7 9
    388J4 970
    88J88 120
    78J69 133
    AT3T7 506
    K9929 960
    2QTJK 976
    28289 935
    84AJ4 940
    328J2 577
    7344J 525
    77997 715
    3J6J6 925
    949J6 277
    9TAT9 733
    235QA 920
    8J282 409
    825KJ 364
    7Q7Q7 212
    54772 315
    7AA7A 104
    6A985 547
    22228 129
    K5KJK 693
    252T8 726
    K3KK3 176
    33377 560
    3KK2J 132
    4J875 183
    92T47 68
    5Q376 748
    T46KT 313
    4JJ8J 801
    745Q7 585
    Q2K47 549
    43525 875
    AKK79 41
    T8858 580
    Q6976 688
    8A8AA 761
    2K6KK 168
    4848T 788
    53232 988
    T9389 869
    KJ34J 96
    77T44 859
    J8333 300
    J5J35 122
    923KK 57
    88828 352
    3864J 529
    4AQQK 608
    33QT3 378
    33759 530
    444Q4 937
    72454 450
    77A66 831
    2K22A 790
    485A5 52
    98T68 208
    78772 481
    QQ7QQ 73
    95A59 479
    93T52 780
    995KK 192
    96J66 670
    59995 105
    72575 764
    T3769 422
    86656 739
    55Q55 508
    J6569 658
    6QQT2 145
    A9T68 232
    JKK92 691
    TT82Q 463
    J4477 591
    K5AK3 648
    22552 676
    355QQ 142
    3T7A2 444
    TQTTT 316
    JJJJQ 844
    64729 666
    TKTKK 568
    K2975 198
    K8K99 430
    5855J 660
    J4A43 583
    2224T 85
    727Q2 146
    T4JTK 308
    4J3KQ 784
    QAJAQ 311
    45884 191
    484K8 649
    JAA66 592
    49848 87
    AATAT 231
    57QQ5 55
""".trimIndent()