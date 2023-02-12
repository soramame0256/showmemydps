# ShowMeMyDPS
特定期間内のダメージ表記を集計するmodです。

以下の戦闘開始後に自動で集計され、戦闘終了後にチャット欄にその結果を表示します。
<br>
･Raidボス(Grootslang･Orphion･Canyon Colossus･Greg)との戦闘中
<br>
･Legendary Islands

DPS:戦闘中の平均DPS
<br>
(戦闘中に出現した-から始まるアーマースタンド名の数値の合計を、総戦闘時間で割ったもの)

Avg/hit:戦闘中の1hitあたりの平均ダメージ
<br>
(戦闘中に出現した-から始まるアーマースタンド名の数値の合計を、そのアーマースタンド数で割ったもの)

Total:戦闘中に出したDamageの合計
<br>
(戦闘中に出現した-から始まるアーマースタンド名の数値を合計したもの)


# 注意事項
ダメージ表記すべてを集計する関係上、以下のものも集計されます。
<br>
・敵の攻撃をプレイヤーが受けた被Damage
<br>
・戦闘中に出現するminion(Greater One等)への与Damage

また以下の時間もDPSの時間として集計されます。
<br>
・Watched中など相手が無敵の時間や、自分が逃げ回っている時
<br>
・Legendary Islandの休憩部屋に居る時間


よって出力されるダメージ統計等は誤差が確定で発生するのでその点はご了承ください。

# 使い方
・Raid Boss/Legendary Islandの場合
<br>
戦闘開始後に自動で集計を開始します。
<br>
・手動の場合
<br>
/showdpsで出力され、/showdps resetによりリセットと出力が行われます