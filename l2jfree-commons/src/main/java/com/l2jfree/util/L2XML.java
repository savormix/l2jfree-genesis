/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jfree.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.w3c.dom.Node;

/**
 * @author NB4L1
 */
public final class L2XML
{
	private static class XMLIterator implements Iterator<Node>
	{
		private Node _node;
		
		public XMLIterator(Node root)
		{
			_node = validate(root.getFirstChild());
		}
		
		private Node validate(Node node)
		{
			for (;;)
			{
				if (node == null || accept(node))
					return node;
				
				node = node.getNextSibling();
			}
		}
		
		protected boolean accept(Node node)
		{
			return true;
		}
		
		@Override
		public final boolean hasNext()
		{
			return _node != null;
		}
		
		@Override
		public final Node next()
		{
			if (!hasNext())
				throw new NoSuchElementException();
			
			final Node node = _node;
			
			_node = validate(_node.getNextSibling());
			
			return node;
		}
		
		@Override
		public final void remove()
		{
			throw new UnsupportedOperationException();
		}
	}
	
	public static Iterable<Node> listNodes(final Node root)
	{
		return new Iterable<Node>() {
			@Override
			public Iterator<Node> iterator()
			{
				return new XMLIterator(root);
			}
		};
	}
	
	public static Iterable<Node> listNodesByNodeName(final Node root, final String nodeName)
	{
		return new Iterable<Node>() {
			@Override
			public Iterator<Node> iterator()
			{
				return new XMLIterator(root) {
					@Override
					protected boolean accept(Node node)
					{
						return node.getNodeName().equals(nodeName);
					}
				};
			}
		};
	}
	
	public static Iterable<Node> listNodesByNodeType(final Node root, final short nodeType)
	{
		return new Iterable<Node>() {
			@Override
			public Iterator<Node> iterator()
			{
				return new XMLIterator(root) {
					@Override
					protected boolean accept(Node node)
					{
						return node.getNodeType() == nodeType;
					}
				};
			}
		};
	}
}
