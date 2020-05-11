from flask import Flask
from flask import request, session
from flask import redirect, url_for, render_template

app = Flask(__name__)
app.secret_key = b'bruh'

boards = []


@app.route('/show_board', methods=['GET', 'POST'])
def show_board():
    if 'current' not in session or ('current' in session and session['current'] > (len(boards) -1)):
        session['current'] = 0

    if 'next' in request.form and session['current'] < (len(boards) - 1):
        session['current'] = session['current'] + 1
    elif 'prev' in request.form and session['current'] > 0:
        session['current'] = session['current'] - 1

    board = ''
    if len(boards) > 0:
        board = boards[session['current']]

    auto_turn = True if 'auto-turn' in request.form and request.form['auto-turn'] == 'on' and 'won' not in board else False

    return render_template('board.html', board=board, auto_turn=auto_turn, step=f"{session['current']} / {len(boards) - 1}")


@app.route('/post_board', methods=['POST', 'GET'])
def update_board():
    if request.method == 'POST':
        if len(boards) > 0 and 'won' in boards[-1]:
            boards.pop()

        next = request.get_json(silent=True)
        boards.append(next)

        if 'Goal' not in next['board'][:(next['boardWidth']*next['goalAreaHeight'])]:
            boards.append({'won': 'red'})
        if 'Goal' not in next['board'][(next['boardWidth']*(next['goalAreaHeight']+next['taskAreaHeight'])):]:
            boards.append({'won': 'blue'})

        # print(request.get_json(silent=True), flush=True)
    return str(len(boards))


@app.route('/')
def index():
    return redirect(url_for('show_board'))


if __name__ == '__main__':
    app.run()
