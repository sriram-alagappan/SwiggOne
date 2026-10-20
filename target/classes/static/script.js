// Update these paths if your controller mappings use different URLs.
const API = { foods: "/api/food-items", customers: "/api/customers", orders: "/api/orders" };
const sampleMenu = [
  {id:"demo-1",name:"Masala Dosa",description:"Crisp dosa with potato masala and chutney.",price:90,vegetarian:true,emoji:"🥞",bg:"#f4e3c5"},
  {id:"demo-2",name:"Paneer Rice Bowl",description:"Warm rice, soft paneer and a gently spiced gravy.",price:180,vegetarian:true,emoji:"🍛",bg:"#e7e8d4"},
  {id:"demo-3",name:"Veg Burger",description:"Crunchy veggie patty, lettuce and house sauce.",price:130,vegetarian:true,emoji:"🍔",bg:"#f2d9c7"},
  {id:"demo-4",name:"Idli & Sambar",description:"Soft idlis with hot sambar and coconut chutney.",price:60,vegetarian:true,emoji:"🥣",bg:"#e6e4d7"},
  {id:"demo-5",name:"Chicken Biryani",description:"Fragrant basmati rice with warming spices.",price:220,vegetarian:false,emoji:"🍲",bg:"#f1dfc3"},
  {id:"demo-6",name:"Fresh Lime Soda",description:"A bright, fizzy cooler for the side.",price:55,vegetarian:true,emoji:"🍋",bg:"#e0e9ce"}
];
const state={foods:[],cart:new Map(),filter:"all",search:""};
const $=s=>document.querySelector(s);
function val(o,keys,fallback){for(const k of keys)if(o&&o[k]!=null)return o[k];return fallback}
function escapeHtml(s){return String(s).replace(/[&<>"']/g,c=>({"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#39;"}[c]))}
function money(n){return new Intl.NumberFormat("en-IN",{style:"currency",currency:"INR",maximumFractionDigits:2}).format(Number(n)||0)}
function normalize(o,i){return {...o,id:String(val(o,["id","foodItemId","itemId","foodId"],"item-"+i)),name:String(val(o,["name","foodName","itemName","title"],"Food item "+(i+1))),description:String(val(o,["description","details"],"Freshly made and ready to enjoy.")),price:Number(val(o,["price","cost","amount"],0)),vegetarian:Boolean(val(o,["vegetarian","isVegetarian","veg"],true)),available:val(o,["available","isAvailable","availability"],true)!==false,emoji:["🥞","🍛","🍔","🥣","🍲","🍋"][i%6],bg:["#f4e3c5","#e7e8d4","#f2d9c7","#e6e4d7","#f1dfc3","#e0e9ce"][i%6]}}
async function loadFoods(){
 try{
  const r=await fetch(API.foods,{headers:{Accept:"application/json"}});if(!r.ok)throw Error("API returned "+r.status);
  const data=await r.json(),list=Array.isArray(data)?data:val(data,["content","data","items","foodItems"],[]);
  if(!Array.isArray(list))throw Error("Unexpected response");
  state.foods=list.map(normalize);$("#notice").textContent=state.foods.length?"":"API connected, but no food items were returned.";
 }catch(e){state.foods=sampleMenu;$("#notice").textContent="Sample menu preview: the food API is not reachable yet. Start Spring Boot to load database items.";console.info("Food API:",e.message)}
 renderFoods()
}
function renderFoods(){
 const q=state.search.toLowerCase().trim(),items=state.foods.filter(f=>f.available&&(!q||(f.name+" "+f.description).toLowerCase().includes(q))&&(state.filter==="all"||(state.filter==="veg"&&f.vegetarian)||(state.filter==="under200"&&f.price<200)));
 $("#foods").innerHTML=items.length?items.map(f=>`<article class="food-card"><div class="food-art" style="--bg:${f.bg}"><b class="tag">${f.vegetarian?"VEG PICK":"NON-VEG"}</b><span>${f.emoji}</span></div><div class="details"><div class="title-row"><h3>${escapeHtml(f.name)}</h3><span class="price">${money(f.price)}</span></div><p class="description">${escapeHtml(f.description)}</p><div class="card-bottom"><span class="veg">${f.vegetarian?"● Vegetarian":"● Chef's special"}</span><button class="add" data-add="${escapeHtml(f.id)}">+ Add</button></div></div></article>`).join(""):'<div class="empty">Nothing matches that search. Try another one.</div>';
}
function add(id){const f=state.foods.find(x=>x.id===id);if(!f)return;const e=state.cart.get(id);state.cart.set(id,{food:f,qty:e?e.qty+1:1});renderCart();toast(f.name+" added to your basket")}
function change(id,n){const e=state.cart.get(id);if(!e)return;e.qty+=n;if(e.qty<=0)state.cart.delete(id);renderCart()}
function total(){return [...state.cart.values()].reduce((s,e)=>s+e.food.price*e.qty,0)}
function renderCart(){const entries=[...state.cart.entries()];$("#count").textContent=entries.reduce((s,[,e])=>s+e.qty,0);$("#total").textContent=$("#dialog-total").textContent=money(total());$("#checkout").disabled=!entries.length;$("#cart").innerHTML=entries.length?entries.map(([id,e])=>`<div class="cart-line"><div class="cart-emoji">${e.food.emoji}</div><div><h3>${escapeHtml(e.food.name)}</h3><p>${money(e.food.price)} each</p></div><div class="qty"><button data-change="${escapeHtml(id)}" data-n="-1">−</button>${e.qty}<button data-change="${escapeHtml(id)}" data-n="1">+</button></div></div>`).join(""):'<div class="empty">Your basket is empty for now.<br>Add something tasty from the menu.</div>'}
function showCart(){ $("#drawer").classList.add("open");$("#shade").classList.add("open")}
function hideCart(){ $("#drawer").classList.remove("open");$("#shade").classList.remove("open")}
let toastTimer;function toast(t){$("#toast").textContent=t;$("#toast").classList.add("show");clearTimeout(toastTimer);toastTimer=setTimeout(()=>$("#toast").classList.remove("show"),2100)}
$("#foods").addEventListener("click",e=>{const b=e.target.closest("[data-add]");if(b)add(b.dataset.add)});
$("#cart").addEventListener("click",e=>{const b=e.target.closest("[data-change]");if(b)change(b.dataset.change,Number(b.dataset.n))});
$("#open-cart").onclick=showCart;$("#close-cart").onclick=hideCart;$("#shade").onclick=hideCart;
$("#search").addEventListener("input",e=>{state.search=e.target.value;renderFoods()});
document.querySelector(".filters").addEventListener("click",e=>{const b=e.target.closest("[data-filter]");if(!b)return;state.filter=b.dataset.filter;document.querySelectorAll("[data-filter]").forEach(x=>x.classList.toggle("chosen",x===b));renderFoods()});
$("#checkout").onclick=()=>{if(!state.cart.size)return;hideCart();$("#message").textContent="";$("#checkout-dialog").showModal()};
$("#close-dialog").onclick=()=>$("#checkout-dialog").close();
$("#checkout-form").addEventListener("submit",async e=>{
 e.preventDefault();if(!state.cart.size)return;
 const customerId=Number($("#customer").value),message=$("#message"),button=$("#place");
 if(!Number.isInteger(customerId)||customerId<1){message.textContent="Enter a valid existing customer ID.";return}
 // IMPORTANT: compare this JSON with FoodOrderRequest.java and change its fields if needed.
 const payload={customerId,items:[...state.cart.values()].map(({food,qty})=>({foodItemId:Number(food.id),quantity:qty}))};
 button.disabled=true;button.textContent="Sending order…";message.textContent="";
 try{
  const r=await fetch(API.orders,{method:"POST",headers:{"Content-Type":"application/json",Accept:"application/json"},body:JSON.stringify(payload)});
  const raw=await r.text();let result=raw;try{result=raw?JSON.parse(raw):{}}catch(_){}
  if(!r.ok)throw Error(typeof result==="string"?result:val(result,["message","error","detail"],"Request failed ("+r.status+")"));
  const id=val(result,["id","orderId","foodOrderId"],null);message.style.color="#465d42";message.textContent=id?`Order placed. Order ID: ${id}`:"Order request submitted.";state.cart.clear();renderCart();toast("Order submitted");
 }catch(err){message.style.color="#c64b25";message.textContent="Could not place order: "+err.message+". Check the API path and FoodOrderRequest fields in script.js."}
 finally{button.disabled=false;button.innerHTML='Place order <span>→</span>'}
});
renderCart();loadFoods();
