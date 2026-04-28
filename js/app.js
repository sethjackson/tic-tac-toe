class Side {
  static X = new Side("X");
  static O = new Side("O");

  #value;

  constructor(value) {
    this.#value = value;
  }

  flip() {
    if (this.#value === "X") {
      return Side.O;
    }

    return Side.X;
  }

  toString() {
    return this.#value;
  }
}

class Board {
  static WINNERS = [
    [0, 1, 2],
    [3, 4, 5],
    [6, 7, 8],
    [0, 3, 6],
    [1, 4, 7],
    [2, 5, 8],
    [0, 4, 8],
    [2, 4, 6],
  ];

  static CENTER = 4;
  static CORNERS = [0, 2, 6, 8];
  static EDGES = [1, 3, 5, 7];

  constructor() {
    this.pieces = new Array(9);
    this.pieces.fill(undefined, 0, 9);
  }

  makeMove(position, side) {
    this.pieces[position] = side;
  }

  undoMove(position) {
    this.pieces[position] = undefined;
  }

  hasPiece(position) {
    return this.pieces[position] != undefined;
  }

  getAvailableMoves() {
    const availableMoves = [];

    for (const [index, piece] of this.pieces.entries()) {
      if (piece !== Side.X && piece !== Side.O) {
        availableMoves.push(index);
      }
    }

    return availableMoves;
  }

  winner() {
    for (const winners of Board.WINNERS) {
      if (this.pieces[winners[0]] === this.pieces[winners[1]] && this.pieces[winners[1]] === this.pieces[winners[2]]) {
        return this.pieces[winners[0]];
      }
    }

    return null;
  }

  isEmpty() {
    return this.pieces.every(piece => piece === undefined);
  }

  isFull() {
    return this.pieces.every(piece => piece !== undefined);
  }

  hasWinner() {
    return this.winner() != null;
  }
}

class Player {
  constructor(side) {
    this.side = side;
  }
}

class ComputerPlayer extends Player {
  #moveGenerator;

  constructor(side, moveGenerator) {
    super(side);

    this.#moveGenerator = moveGenerator;
  }

  getMove(board) {
    return this.#moveGenerator.getMove(board, this.side);
  }
}

class RandomMoveGenerator {
  getMove(board, side) {
    const availableMoves = board.getAvailableMoves();

    return availableMoves[Math.floor(Math.random() * availableMoves.length)];
  }
}

class RuleBasedGenerator {
  getMove(board, side) {
    // Based on the rules here: https://en.wikipedia.org/wiki/Tic-tac-toe#Strategy
    // 1. Win if possibe
    // 2. Block the opponents win
    // 3. Play the center
    // 4. Play the corners
    // 5. Play the edges
    const winner = this.#getWinner(board, side);
    if (winner) {
      return winner;
    }

    const opponentWinner = this.#getWinner(board, side.flip());
    if (opponentWinner) {
      return opponentWinner;
    }

    if (!board.hasPiece(Board.CENTER)) {
      return Board.CENTER;
    }

    for (const corner of Board.CORNERS) {
      if (!board.hasPiece(corner)) {
        return corner;
      }
    }

    for (const edge of Board.EDGES) {
      if (!board.hasPiece(edge)) {
        return edge;
      }
    }
  }

  #getWinner(board, side) {
    for (const winners of Board.WINNERS) {
      let occupied = 0;
      let emptySlot;
      for (const winner of winners) {
        if (board.pieces[winner] === side) {
          occupied += 1;
        } if (!board.pieces[winner]) {
          emptySlot = winner;
        }
      }

      if (emptySlot && occupied === 2) {
        return emptySlot;
      }
    }

    return null;
  }
}

class NegamaxMoveGenerator {
  getMove(board, side) {
    const availableMoves = board.getAvailableMoves();

    let move = -1;
    let max = -Infinity;

    for (const position of availableMoves) {
      board.makeMove(position, side);

      let score = -this.#negamax(board, availableMoves.length - 1, side.flip());

      board.undoMove(position);

      if (score > max) {
        max = score;
        move = position;
      }
    }

    return move;
  }

  #negamax(board, depth, sideToMove) {
    if (depth === 0) {
      const winner = board.winner();
      if (winner === sideToMove) {
        return Infinity;
      } else if (winner === sideToMove.flip()) {
        return -Infinity;
      }

      return 0;
    }

    let max = -Infinity;

    for (const position of board.getAvailableMoves()) {
      board.makeMove(position, sideToMove);

      let score = -this.#negamax(board, depth - 1, sideToMove.flip());

      board.undoMove(position);

      if (score > max) {
        max = score;
      }
    }

    return max;
  }
}

class Game {
  #x;
  #o;
  #undoStack;
  #redoStack;

  constructor(x, o) {
    this.#x = x;
    this.#o = o;

    this.playerToMove = x;

    this.board = new Board();

    this.#undoStack = [];
    this.#redoStack = [];
  }

  makeMove(position) {
    if (this.isOver()) {
      return;
    }

    this.#redoStack = [];

    this.#makeMoveInternal(position);
  }

  #makeMoveInternal(position) {
    this.board.makeMove(position, this.playerToMove.side);

    this.#undoStack.push(position);

    if (this.playerToMove === this.#x) {
      this.playerToMove = this.#o;
    } else {
      this.playerToMove = this.#x;
    }
  }

  undoMove() {
    if (!this.canUndoMove()) {
      return;
    }

    const position = this.#undoStack.pop();

    this.board.undoMove(position);

    this.#redoStack.push(position);

    if (this.playerToMove === this.#x) {
      this.playerToMove = this.#o;
    } else {
      this.playerToMove = this.#x;
    }
}

  redoMove() {
    if (!this.canRedoMove()) {
      return;
    }

    this.#makeMoveInternal(this.#redoStack.pop());
  }

  canUndoMove() {
    return this.#undoStack.length !== 0;
  }

  canRedoMove() {
    return this.#redoStack.length !== 0;
  }

  inProgress() {
    return !this.board.isEmpty() && !this.isOver();
  }

  isOver() {
    return this.board.hasWinner() || this.board.isFull();
  }

  winner() {
    const winner = this.board.winner();

    if (winner === Side.X) {
      return this.#x;
    } else if (winner === Side.O) {
      return this.#o;
    }

    return null;
  }
}

document.addEventListener('DOMContentLoaded', () => {
  document.getElementById('new-game-button').addEventListener('click', newGame);
  document.getElementById('x-player').addEventListener('change', playerSelectionChanged);
  document.getElementById('o-player').addEventListener('change', playerSelectionChanged);
});

function newGame() {
  const modal = bootstrap.Modal.getInstance(document.getElementById('new-game-modal'));
  if (modal) {
    modal.hide();
  }

  let x = null;
  let o = null;

  if (document.getElementById('x-player').value === 'human') {
    x = new Player(Side.X);
  } else {
    const xMoveGenerator = getMoveGenerator(document.getElementById('x-move-generator'));
    if (!xMoveGenerator) {
      alert('Bad input!');

      return;
    }

    x = new ComputerPlayer(Side.X, xMoveGenerator);
  }

  if (document.getElementById('o-player').value === 'human') {
    o = new Player(Side.O);
  } else {
    const oMoveGenerator = getMoveGenerator(document.getElementById('o-move-generator'));
    if (!oMoveGenerator) {
      alert('Bad input!');

      return;
    }

    o = new ComputerPlayer(Side.O, oMoveGenerator);
  }

  globalThis.game = new Game(x, o);

  document.getElementById('undo-button').addEventListener('click', undoMove);
  document.getElementById('redo-button').addEventListener('click', redoMove);

  for (const cell of document.getElementsByClassName('cell')) {
    cell.addEventListener('click', makeMove);
  }

  resetSelections();

  gameStateChanged();
}

function resetSelections() {
  const xPlayer = document.getElementById('x-player');
  const xMoveGenerator = document.getElementById('x-move-generator');
  const oPlayer = document.getElementById('o-player');
  const oMoveGenerator = document.getElementById('o-move-generator');

  xPlayer.selectedIndex = 0;
  xMoveGenerator.selectedIndex = 0;
  xMoveGenerator.disabled = true;

  oPlayer.selectedIndex = 0;
  oMoveGenerator.selectedIndex = 0;
  oMoveGenerator.disabled = true;
}

function getMoveGenerator(element) {
  const type = element.value;
  switch (type) {
    case 'negamax':
      return new NegamaxMoveGenerator();
    case 'rule-based':
      return new RuleBasedGenerator();
    case 'random':
      return new RandomMoveGenerator();
  }

  return null;
}

function gameStateChanged() {
  const game = globalThis.game;
  if (!game) {
    return;
  }

  document.getElementById('undo-button').disabled = !game.canUndoMove()
  document.getElementById('redo-button').disabled = !game.canRedoMove();

  for (const [i, piece] of game.board.pieces.entries()) {
    const cell = document.getElementsByClassName('cell')[i];
    if (piece) {
      cell.classList.add('disabled')
      cell.innerHTML = piece;
    } else {
      cell.classList.remove('disabled');
      cell.innerHTML = '';
    }
  }

  if (game.isOver()) {
    for (const cell of document.getElementsByClassName('cell')) {
      cell.classList.add('disabled');
    }

    requestAnimationFrame(() => {
      requestAnimationFrame(showGameOverModal)
    });
  } else {
    const playerToMove = game.playerToMove;

    if (playerToMove instanceof ComputerPlayer) {
      const move = playerToMove.getMove(game.board);

      game.makeMove(move);

      gameStateChanged();
    }
  }
}

function showGameOverModal() {
  const game = globalThis.game;
  if (!game) {
    return;
  }

  const winner = game.winner();
  if (winner) {
    alert(`${winner.side} won!`);
  } else {
    alert('Draw');
  }
}

function undoMove() {
  const game = globalThis.game;
  if (!game) {
    return;
  }

  game.undoMove();

  gameStateChanged();
}

function redoMove() {
  const game = globalThis.game;
  if (!game) {
    return;
  }

  game.redoMove();

  gameStateChanged();
}

function makeMove() {
  const game = globalThis.game;
  if (!game || game.isOver()) {
    return;
  }

  game.makeMove(this.id - 1);

  gameStateChanged();
}

function playerSelectionChanged(element) {
  const side = element.target.id.startsWith('x') ? 'x' : 'o';
  const moveGeneratorElement = document.getElementById(`${side}-move-generator`);

  const type = element.target.value;
  if (type === 'computer') {
    moveGeneratorElement.disabled = false
  } else {
    moveGeneratorElement.disabled = true;
  }
}
