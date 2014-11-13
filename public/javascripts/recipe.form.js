var substringMatcher = function(strs) {
  return function findMatches(q, cb) {
    var matches, substrRegex;
    matches = [];
    substrRegex = new RegExp(q, 'i');
    $.each(strs, function(i, str) {
      if (substrRegex.test(str.name)) {
        matches.push({ name: str.name });
      }
    });
    cb(matches);
  };
};

window.loadedIngredients = false;
window.loadedTags = false;

$(document).ready(function() {

  var interval = setInterval(function() {
    var loaded = !!window.ingredients && !!window.availableTags;
    if (loaded) {
      clearInterval(interval);
      $('h1').append(' (loaded)');
    }
  }, 1000);

  var minutes = function(i) {
    var min = 10;
    return min + i * 10;
  }

  var hours = function(minutes) {
    if (minutes < 60) return minutes+"m";
    var h = parseInt(minutes / 60);
    return h +"t "+ (minutes - h * 60)+"m"
  }

  for (var i = 0; i < 14; i++) {
    $('.time').append('<label>'+
    '  <input type="radio" name="button-group-time" value="'+minutes(i)+'">'+
    '  <span class="button-group-item">'+hours(minutes(i))+'</span>'+
    '</label>');
  }
  
  $.get('/api/ingredients', function(response) {
    window.ingredients = [];

    $.each(response.ingredients, function(i, ingredient) {
      window.ingredients.push(ingredient.name);
    })

    $('.ingredient-name').autocomplete({
      source: window.ingredients
    });
  });

  $.get('/api/tags', function(response) {
    window.availableTags = [];

    $.each(response.tags, function(i, tag) {
      window.availableTags.push(tag.name);
    });

    function split( val ) {
      return val.split( /,\s*/ );
    }
    function extractLast( term ) {
      return split( term ).pop();
    }
 
    $('#tags')
      .bind('keydown', function(e) {
        if ( e.keyCode === $.ui.keyCode.TAB &&
            $( this ).autocomplete( "instance" ).menu.active ) {
          e.preventDefault();
        }
      })
      .autocomplete({
        minLength: 0,
        source: function( request, response ) {
          response( $.ui.autocomplete.filter(
            window.availableTags, extractLast( request.term ) ) );
        },
        focus: function() {
          return false;
        },
        select: function( event, ui ) {
          var terms = split( this.value );
          terms.pop();
          terms.push( ui.item.value );
          terms.push( "" );
          this.value = terms.join( ", " );
          return false;
        }
      });
  });
});

function readURL(input) {
  if (input.files && input.files[0]) {
    var reader = new FileReader();
    reader.onload = function (e) {
      $('#preview').attr('src', e.target.result);
    }
    reader.readAsDataURL(input.files[0]);
  }
}

$("[type=file]").change(function(){
    readURL(this);
});

$('[data-js-new-ingredient]').click(function() {
  $('.ingredients-wrapper').append('<div class="ingredient"><div class="span1of3">'+
     '   <input type="text" class="ingredient-name" placeholder="Plomme">'+
     ' </div>'+
     ' <div class="span1of3">'+
     '   <input type="number" class="ingredient-amount" min="0" placeholder="Antall/mengde">'+
     ' </div>'+
     ' <div class="span1of3 last">'+
     '   <select class="ingredient-unit">'+
     '     <option value="gram">Gram</option>'+
     '     <option value="cup">Kopp</option>'+
     '     <option value="liter">Liter</option>'+
     '     <option value="stk">Stk</option>'+
     '     <option value="pinch">Klype</option>'+
     '     <option value="basket">Kurv</option>'+
     '   </select>'+
     ' </div></div>');

  $( ".ingredient-name:last" ).autocomplete({
    source: window.ingredients
  });
});

$('input[type=submit]').click(function() {
  var name = $('#name').val();
  var tags = $('#tags').val();
  if (tags !== "") { tags = tags.split(','); } else { tags = []; }
  var time = parseInt($('[name=button-group-time]:checked').val());
  var language = parseInt($('#language option:selected').val());

  // picture

  var description = $('#description').val();
  var procedure = $('#procedure').val();
  var strength = parseInt($('[name=button-group-strength]:checked').val());
  var ingredients = [];

  var $ing = $('.ingredient');
  $ing.each(function(index, $ingredient) {
    var ingredientname = $('.ingredient-name', $ingredient).val();
    var amount = $('.ingredient-amount', $ingredient).val();
    var unit = $('.ingredient-unit option:selected', $ingredient).val();

    if(!amount) { return; }

    ingredients.push({
      name: ingredientname,
      amount: parseFloat(amount),
      unit: unit
    });
  });

  var firstName = $('#first-name').val();
  var lastName = $('#last-name').val();
  var email = $('#email').val();

  var postObject = {
    name: name,
    tags: tags,
    time: time,
    language: language,
    description: description,
    procedure: procedure,
    strength: strength,
    ingredients: ingredients,
    createdBy: {
      id: 1,
      name: firstName + ' ' + lastName
    }
  };

  window.postObject = postObject;

  $('#result').html('<b>The following is going to be posted</b>\n' + 
    JSON.stringify(postObject, null, '\t') + 
    '\n\n<button class="send">Send inn oppskrift</button>');

  $('.send').click(function() {
    console.log(arguments);

    var xhr = $.ajax({
      url: 'api/recipes', 
      data: JSON.stringify(postObject),
      type: 'POST',
      contentType: 'text/json'
    });

    xhr.done(function(response) {
      alert('done', JSON.stringify(response));
      location.reload();
    });

    xhr.fail(function() {

      console.log(arguments);
    });
  });

})